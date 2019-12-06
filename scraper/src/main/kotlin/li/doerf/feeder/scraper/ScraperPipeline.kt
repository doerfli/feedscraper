package li.doerf.feeder.scraper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.util.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

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
            generateFeedUrls()
                    // download feeds
                    .map { url -> feedDownloaderStep.download(url) }
                    .filter { result -> result is DownloadSuccess }
                    .map { it as DownloadSuccess }
                    .buffer()

                    // parse downloaded feeds
                    .map { (url, content) -> feedParserStep.parse(url, content) }
                    .filter { result -> result is ParserSuccess }
                    .map { it as ParserSuccess }

                    // persist feeds
                    .map { (url, feedDto) ->
                        feedPersisterStep.persist(url, feedDto) }
                    .filter { result -> result is PersisterSuccess }
                    .map { it as PersisterSuccess }

                    // notify clients
                    .map{ (firstDownload, itemsUpdated, feedPkey) ->
                        feedNotifierStep.sendMessage(feedPkey, firstDownload, itemsUpdated)
                    }
                    .toList()
        }
    }

    fun generateFeedUrls() = flow<String> {
        log.info("starting feed url generator")
        while (true) {
            val startedAt = Instant.now()
            feedRepository.findNotRetryExceeded().forEach { feed ->
                emit(feed.url)
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