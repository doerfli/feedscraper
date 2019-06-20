package li.doerf.feeder.scraper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.scraper.dto.FeedDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScraperPipeline @Autowired constructor(
        private val feedRepository: FeedRepository,
        private val feedDownloaderStep: FeedDownloaderStep,
        private val feedParserStep: FeedParserStep,
        private val feedPersisterStep: FeedPersisterStep
) {

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
            val feedUrlProducer = produceFeedUrls()

            val parserChannel = Channel<Pair<String, String>>()
            val persisterChannel = Channel<Pair<String, FeedDto>>()
            repeat(2) { launchFeedUrlDownloader(it, feedUrlProducer, parserChannel) }
            repeat(2) { launchFeedParser(it, parserChannel, persisterChannel) }
            launchFeedPersister(0, persisterChannel)
        }
    }

    fun CoroutineScope.produceFeedUrls() = produce {
        log.info("starting feed url producer")
        while (true) {
            feedRepository.findAll().forEach {feed ->
                send(feed.url)
            }
            delay(60000) // TODO put in config
        }
    }

    suspend fun CoroutineScope.launchFeedUrlDownloader(id: Int, channel: ReceiveChannel<String>, parserChannel: Channel<Pair<String, String>>) = launch {
        log.info("starting feed downloader #$id")
        for (uri in channel) {
            log.debug("FeedDto Downloader #$id received uri $uri")
            try {
                val feedAsString = feedDownloaderStep.download(uri)
                // TODO handle error in downloader
                parserChannel.send(Pair(uri, feedAsString))
            } catch (e: Exception) {
                log.error("caught Exception while downloading uri $uri", e)
            }
        }
    }

    fun CoroutineScope.launchFeedParser(id: Int, channel: ReceiveChannel<Pair<String,String>>, persisterChannel: Channel<Pair<String, FeedDto>>) = launch {
        log.info("starting feed parser #$id")
        for ((uri, feedAsString) in channel) {
            log.debug("FeedDto Parser #$id received content for $uri")
            try {
                val feed = feedParserStep.parse(uri, feedAsString)
                persisterChannel.send(Pair(uri, feed))
            } catch (e: Exception) {
                log.error("caught Exception while parsing uri $uri", e)
            }
        }
    }

    fun CoroutineScope.launchFeedPersister(id: Int, channel: ReceiveChannel<Pair<String, FeedDto>>) = launch {
        log.info("starting feed persister #$id")
        for ((uri, feed) in channel) {
            log.debug("FeedDto Perister #$id received feed for $uri")
            try {
                feedPersisterStep.persist(uri, feed)
            } catch (e: Exception) {
                log.error("caught Exception while persisting uri $uri", e)
            }
        }
    }

    private fun addDefaultEntries() {
        val urls = listOf("https://www.heise.de/rss/heise-top-atom.xml", "https://www.heise.de/rss/heise-atom.xml", "http://www.spiegel.de/schlagzeilen/tops/index.rss")
        urls.forEach {
            if (feedRepository.countFeedsByUrl(it) == 0) {
                val feed = Feed(pkey = 0L, url = it)
                feedRepository.save(feed)
                log.debug("stored feed $feed")
            }
        }
    }

}