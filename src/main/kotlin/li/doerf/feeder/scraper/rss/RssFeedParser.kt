package li.doerf.feeder.scraper.rss

import li.doerf.feeder.scraper.dto.Entry
import li.doerf.feeder.scraper.dto.Feed
import li.doerf.feeder.scraper.dto.FeedSource
import li.doerf.feeder.scraper.util.getLogger
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.Instant
import javax.xml.parsers.SAXParserFactory

class RssFeedParser : DefaultHandler() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    private lateinit var feed: Feed
    private var isItemParsing = false
    private var isImageParsing = false

    private lateinit var id: String
    private lateinit var title: String
    private lateinit var description: String
    private lateinit var content: String
    private lateinit var link: String
    private lateinit var published: Instant

    private var parsedString = StringBuffer()

    fun parse(input: InputStream): Feed {
        if (::feed.isInitialized) {
            throw IllegalStateException("parse may only be called once")
        }
        val factory = SAXParserFactory.newInstance()
        val parser = factory.newSAXParser()
        log.debug("starting to parse rss feed")
        parser.parse(input, this)
        log.debug("finished to parse rss feed")
        return feed
    }

    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if(log.isTraceEnabled) log.trace("startElement: $qName")
        parsedString = StringBuffer()
        when(qName) {
            "channel" -> feed = Feed(source = FeedSource.RSS)
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
                feed.entries.add(Entry(id, title, link, description, content, published, published))
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
                val dateParser = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss X")
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

    private fun getParsedString() = parsedString.toString().trim()

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        if (ch == null) {
            return
        }
        val t = ch.sliceArray(IntRange(start, start + length - 1)) // IntRange end is inclusive
        parsedString.append(t)
    }
}