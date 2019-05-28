package li.doerf.feeder.scraper

import li.doerf.feeder.scraper.atom.AtomFeedParser
import li.doerf.feeder.scraper.dto.FeedDto
import li.doerf.feeder.scraper.entities.FeedSourceType
import li.doerf.feeder.scraper.entities.Feed
import li.doerf.feeder.scraper.repositories.FeedRepository
import li.doerf.feeder.scraper.rss.RssFeedParser
import li.doerf.feeder.scraper.util.getLogger
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

        if (feed.sourceType == null) {
            determineFeedSourceType(feed, feedAsString)
        }

        return when(feed.sourceType) {
            FeedSourceType.Atom -> AtomFeedParser().parse(ByteArrayInputStream(feedAsString.toByteArray()))
            FeedSourceType.RSS -> RssFeedParser().parse(ByteArrayInputStream(feedAsString.toByteArray()))
            else -> throw IllegalArgumentException("unknown sourceType ${feed.sourceType}")
        }
    }

    private fun determineFeedSourceType(feed: Feed, feedAsString: String) {
        log.debug("determining feed type")
        if (feedAsString.contains(atomStartRegex) && feedAsString.contains(atomEndRegex)) {
            feed.sourceType = FeedSourceType.Atom
        } else if (feedAsString.contains(rssStartRegex) && feedAsString.contains(rssEndRegex)) {
            feed.sourceType = FeedSourceType.RSS
        } else {
            throw IllegalStateException("unable to determine tye of feed")
        }
        feedRepository.save(feed)
        log.info("feed is type ${feed.sourceType}")
    }
}
