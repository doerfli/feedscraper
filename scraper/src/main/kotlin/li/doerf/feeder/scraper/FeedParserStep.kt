package li.doerf.feeder.scraper

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.FeedType
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.scraper.atom.AtomFeedParser
import li.doerf.feeder.scraper.dto.FeedDto
import li.doerf.feeder.scraper.rss.RssFeedParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream

@Service
class FeedParserStep @Autowired constructor(
        val feedRepository: FeedRepository
){

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
        private val atomStartRegex = Regex("\\<feed\\s.*[^>]>")
        private val atomEndRegex = Regex("\\<\\/feed>")
        private val rssStartRegex = Regex("\\<rss\\s.*[^>]>")
        private val rssEndRegex = Regex("\\<\\/rss>")
    }

    fun parse(uri: String, feedAsString: String): FeedDto {
        log.debug("parsing feed")
        val feedOpt = feedRepository.findFeedByUrl(uri)
        if (feedOpt.isEmpty) {
            throw IllegalArgumentException("feed with uri $uri not found")
        }
        val feed = feedOpt.get()

        if (feed.type == null) {
            determineFeedSourceType(feed, feedAsString)
        }

        return when(feed.type) {
            FeedType.Atom -> AtomFeedParser().parse(ByteArrayInputStream(feedAsString.toByteArray()))
            FeedType.RSS -> RssFeedParser().parse(ByteArrayInputStream(feedAsString.toByteArray()))
            else -> throw IllegalArgumentException("unknown type ${feed.type}")
        }
    }

    private fun determineFeedSourceType(feed: Feed, feedAsString: String) {
        log.debug("determining feed type")
        if (feedAsString.contains(atomStartRegex) && feedAsString.contains(atomEndRegex)) {
            feed.type = FeedType.Atom
        } else if (feedAsString.contains(rssStartRegex) && feedAsString.contains(rssEndRegex)) {
            feed.type = FeedType.RSS
        } else {
            throw IllegalStateException("unable to determine tye of feed")
        }
        feedRepository.save(feed)
        log.info("feed is type ${feed.type}")
    }
}
