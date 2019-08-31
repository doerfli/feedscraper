package li.doerf.feeder.viewer.services

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.websocket.messages.NewFeedsMessage
import li.doerf.feeder.viewer.websocket.messages.UpdatedItemsMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class FeedServiceImpl @Autowired constructor(
        private val feedRepository: FeedRepository,
        private val wsTemplate: SimpMessagingTemplate
) : FeedService {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    override fun getAllActiveFeeds(): Collection<Feed> {
        return feedRepository.findAllByTitleNotNull(Sort.by(Sort.Order.asc("title").ignoreCase()))
    }

    override fun add(url: String): Feed {
        val feedOpt = feedRepository.findFeedByUrl(url)
        if (feedOpt.isPresent) {
            throw IllegalArgumentException("feed url already exists $url")
        }
        val feed = Feed(0, url)
        feedRepository.save(feed)
        log.info("new feed creared $feed")
        return feed
    }

    override fun notifyClientsAboutNewFeed(msg: String) {
        wsTemplate.convertAndSend("/topic/feeds", NewFeedsMessage("new"))
    }

    override fun notifyClientsUpdatedItems(feedPkey: String) {
        wsTemplate.convertAndSend("/topic/updated_items", UpdatedItemsMessage(feedPkey))
    }

}