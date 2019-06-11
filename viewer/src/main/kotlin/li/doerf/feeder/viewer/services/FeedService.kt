package li.doerf.feeder.viewer.services

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.util.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class FeedService @Autowired constructor(
        val feedRepository: FeedRepository
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

}