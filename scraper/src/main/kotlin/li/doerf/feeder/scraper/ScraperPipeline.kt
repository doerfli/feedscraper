package li.doerf.feeder.scraper

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.scraper.dto.FeedDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import javax.annotation.PreDestroy

@Service
class ScraperPipeline @Autowired constructor(
        private val feedRepository: FeedRepository,
        private val feedDownloaderStep: FeedDownloaderStep,
        private val feedParserStep: FeedParserStep,
        private val feedPersisterStep: FeedPersisterStep,
        private val feedNotifierStep: FeedNotifierStep
) {
    private lateinit var scope: CoroutineScope
    @Value("\${scraper.pipeline.interval:60000}")
    private val interval: Long = 60000

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    fun execute() {
        log.info("Starting scraper pipeline")
        addDefaultEntries()
        startPipeline()
    }

    private fun startPipeline() {
        runBlocking {
            scope = CoroutineScope(Job())
            val feedUrlProducer = produceFeedUrls()

            val parserChannel = Channel<Pair<String, String>>()
            val persisterChannel = Channel<Pair<String, FeedDto>>()
            val notifierChannel = Channel<FeedPersisterResult>()
            repeat(2) { launchFeedUrlDownloader(scope, it, feedUrlProducer, parserChannel) }
            repeat(2) { launchFeedParser(scope, it, parserChannel, persisterChannel) }
            launchFeedPersister(scope, 0, persisterChannel, notifierChannel)
            launchFeedNotifier(scope, 0, notifierChannel)
        }
    }

    @PreDestroy
    fun stopPipeline() {
        log.info("shutting down pipeline")
        if (scope.isActive) {
            scope.cancel()
        }
    }

    fun CoroutineScope.produceFeedUrls() = produce<String> {
        log.info("starting feed url producer")
        while (true) {
            val startedAt = Instant.now()
            feedRepository.findNotRetryExceeded().forEach { feed ->
                send(feed.url)
            }
            waitIfNecessary(startedAt)
        }
    }

    private suspend fun waitIfNecessary(startedAt: Instant?) {
        val remaining = interval - Duration.between(startedAt, Instant.now()).toMillis()
        if (remaining > 0) {
            log.debug("waiting for $remaining ms")
            delay(interval)
        }
    }

    suspend fun launchFeedUrlDownloader(scope: CoroutineScope, id: Int, channel: ReceiveChannel<String>, parserChannel: Channel<Pair<String, String>>) = scope.launch {
        log.info("starting feed downloader #$id")
        for (uri in channel) {
            yield()
            log.debug("FeedDto Downloader #$id received uri $uri")
            try {
                val feedAsString = feedDownloaderStep.download(uri)
                parserChannel.send(Pair(uri, feedAsString))
            } catch (e: Exception) {
                log.error("caught Exception while downloading uri $uri", e)
            }
        }
    }

    fun launchFeedParser(scope: CoroutineScope, id: Int, channel: ReceiveChannel<Pair<String,String>>, persisterChannel: Channel<Pair<String, FeedDto>>) = scope.launch {
        log.info("starting feed parser #$id")
        for ((uri, feedAsString) in channel) {
            yield()
            log.debug("FeedDto Parser #$id received content for $uri")
            parseFeed(uri, feedAsString, persisterChannel)
        }
    }

    internal suspend fun parseFeed(uri: String, feedAsString: String, persisterChannel: Channel<Pair<String, FeedDto>>) {
        try {
            val feed = feedParserStep.parse(uri, feedAsString)
            persisterChannel.send(Pair(uri, feed))
        } catch (e: Exception) {
            log.error("caught Exception while parsing uri $uri", e)
            handleFeedException(uri)
        }
    }

    fun launchFeedPersister(scope: CoroutineScope, id: Int, channel: ReceiveChannel<Pair<String, FeedDto>>, notifierChannel: Channel<FeedPersisterResult>) = scope.launch {
        log.info("starting feed persister #$id")
        for ((uri, feed) in channel) {
            yield()
            log.debug("FeedDto Perister #$id received feed for $uri")
            persistFeed(uri, feed, notifierChannel)
        }
    }

    internal suspend fun persistFeed(uri: String, feed: FeedDto, notifierChannel: Channel<FeedPersisterResult>) {
        try {
            val feedNotification = feedPersisterStep.persist(uri, feed)
            notifierChannel.send(feedNotification)
        } catch (e: Exception) {
            log.error("caught Exception while persisting uri $uri", e)
            handleFeedException(uri)
        }
    }

    fun launchFeedNotifier(scope: CoroutineScope, id: Int, channel: ReceiveChannel<FeedPersisterResult>) = scope.launch {
        log.info("starting feed notifier #$id")
        for (result in channel) {
            yield()
            log.debug("Feed Notification #$id received msg")
            try {
                feedNotifierStep.sendMessage(result)
            } catch (e: Exception) {
                log.error("caught Exception while sending notification", e)
            }
        }
    }

    private fun handleFeedException(uri: String) {
        val feed = feedRepository.findFeedByUrl(uri)
        if (feed.isEmpty) {
            log.error("no feed with uri $uri found")
            return;
        }
        feed.get().retry++
        feedRepository.save(feed.get())
        log.info("retry counter incremented to ${feed.get().retry} for feed with uri $uri")
    }

    private fun addDefaultEntries() {
        val urls = listOf("https://www.heise.de/rss/heise-atom.xml", "http://www.spiegel.de/schlagzeilen/tops/index.rss")
        urls.forEach {
            if (feedRepository.countFeedsByUrl(it) == 0) {
                val feed = Feed(pkey = 0L, url = it)
                feedRepository.save(feed)
                log.debug("stored feed $feed")
            }
        }
    }

}