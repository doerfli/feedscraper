package li.doerf.feeder.scraper.rss

import li.doerf.feeder.common.entities.FeedType
import li.doerf.feeder.scraper.FeedParserBase
import li.doerf.feeder.scraper.dto.FeedDto
import li.doerf.feeder.scraper.dto.ItemDto
import li.doerf.feeder.common.util.getLogger
import org.xml.sax.Attributes
import java.text.SimpleDateFormat
import java.time.Instant

class RssFeedParser : FeedParserBase() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    private var isItemParsing = false
    private var isImageParsing = false

    private lateinit var id: String
    private lateinit var title: String
    private lateinit var description: String
    private lateinit var content: String
    private lateinit var link: String
    private lateinit var published: Instant

    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if(log.isTraceEnabled) log.trace("startElement: $qName")
        parsedString = StringBuffer()
        when(qName) {
            "channel" -> {
                log.debug("creating new rss feed object")
                feed = FeedDto(type = FeedType.RSS)
            }
            "item" -> {
                isItemParsing = true
                id = ""
                title = ""
                description = ""
                content = ""
                link = ""
                published = Instant.MIN
            }
            "image" -> {
                isImageParsing = true
            }
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        if(log.isTraceEnabled) log.trace("endElement: $qName")
        when(qName) {
            "item" -> {
                isItemParsing = false
                feed.items.add(ItemDto(id, title, link, description, content, published, published))
            }
            "image" -> {
                isImageParsing = false
            }
            "title" -> {
                if (!isItemParsing && !isImageParsing) {
                    feed.title = getParsedString()
                } else {
                    title = getParsedString()
                }
            }
            "link" -> {
                if (!isItemParsing) {
                    feed.linkAlternate = getParsedString()
                } else {
                    link = getParsedString()
                }
            }
            "pubDate" ->  {
                val dateParser = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z")
                if (!isItemParsing) {
                    feed.updated = dateParser.parse(getParsedString()).toInstant()
                } else {
                    published = dateParser.parse(getParsedString()).toInstant()
                }
            }
            "description" -> {
                description = getParsedString()
            }
            "guid" -> {
                id = getParsedString()
            }
            "encoded" -> {
                content = getParsedString()
            }
        }
    }
}