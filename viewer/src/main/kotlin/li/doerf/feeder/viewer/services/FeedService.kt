package li.doerf.feeder.viewer.services

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.websocket.messages.NewFeedsMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class FeedService @Autowired constructor(
        private val feedRepository: FeedRepository,
        private val wsTemplate: SimpMessagingTemplate
        ){

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    fun getAllActiveFeeds(): Collection<Feed> {
        return feedRepository.findAllByTitleNotNull(Sort.by(Sort.Order.asc("title").ignoreCase()))
    }

    fun add(url: String): Feed {
        val feedOpt = feedRepository.findFeedByUrl(url)
        if (feedOpt.isPresent) {
            throw IllegalArgumentException("feed url already exists $url")
        }
        val feed = Feed(0, url)
        feedRepository.save(feed)
        log.info("new feed creared $feed")
        return feed
    }

    fun waitForFeedScrapeAndNotifyClient(url: String) {
        val thread = Thread {
            val waitUntil = Instant.now().plusSeconds(300)
            log.debug("waiting for feed to be downloaded")
            while(feedRepository.countFeedsByUrlAndTitleNotNull(url) == 0 && Instant.now().isBefore(waitUntil)) {
                log.debug("feed not available, wait 1s")
                Thread.sleep(1000)
            }
            if (feedRepository.countFeedsByUrlAndTitleNotNull(url) > 0) {
                log.debug("notify client that new feed is active")
                wsTemplate.convertAndSend("/topic/feeds", NewFeedsMessage("new"))
            } else {
                log.warn("feed not downloaded within 5 minutes")
            }
        }
        thread.start()
    }

}