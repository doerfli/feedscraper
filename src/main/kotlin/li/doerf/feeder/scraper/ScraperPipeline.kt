package li.doerf.feeder.scraper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import li.doerf.feeder.scraper.entities.Feed
import li.doerf.feeder.scraper.repositories.FeedRepository
import li.doerf.feeder.scraper.util.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScraperPipeline @Autowired constructor(
        private val feedRepository: FeedRepository,
        private val feedDownloader: FeedDownloader
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
            repeat(2) { launchFeedUrlDownloader(it, feedUrlProducer) }
        }
    }

    fun CoroutineScope.produceFeedUrls() = produce {
        log.info("starting feed url producer")
        while (true) { // TODO repeat every 10s
            feedRepository.findAll().forEach {feed ->
                send(feed.url)
            }
            delay(10000)
        }
    }

    fun CoroutineScope.launchFeedUrlDownloader(id: Int, channel: ReceiveChannel<String>) = launch {
        log.info("starting feed downloader #$id")
        for (msg in channel) {
            log.debug("Feed Downloader #$id received url $msg")
            val feedAsString = feedDownloader.download(msg)
            // TODO handle error in downloader
        }
    }

    private fun addDefaultEntries() {
        val urls = listOf("https://www.heise.de/rss/heise-top-atom.xml", "http://www.spiegel.de/schlagzeilen/tops/index.rss")
        urls.forEach {
            if (feedRepository.countFeedsByUrl(it) == 0) {
                val feed = Feed(pkey = 0L, url = it)
                feedRepository.save(feed)
                log.debug("stored feed $feed")
            }
        }
    }

}