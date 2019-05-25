package li.doerf.feeder.scraper

import li.doerf.feeder.scraper.dto.FeedDto
import li.doerf.feeder.scraper.util.getLogger
import org.springframework.stereotype.Service

@Service
class FeedPersisterStep {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    fun persist(uri: String, feed: FeedDto) {
        log.debug("starting to persist feed for $uri")
    }
}