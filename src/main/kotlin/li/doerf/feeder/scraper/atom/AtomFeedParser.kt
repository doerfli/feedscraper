package li.doerf.feeder.scraper.atom

import li.doerf.feeder.scraper.dto.Entry
import li.doerf.feeder.scraper.dto.Feed
import li.doerf.feeder.scraper.util.getLogger
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.io.InputStream
import java.time.Instant
import javax.xml.parsers.SAXParserFactory


class AtomFeedParser : DefaultHandler() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    private lateinit var feed: Feed
    private var isParsingEntry = false

    private lateinit var id: String
    private lateinit var title: String
    private lateinit var content: String
    private lateinit var summary: String
    private lateinit var link: String
    private lateinit var updated: Instant
    private lateinit var published: Instant
    private var rel: String? = null

    private var parsedString = StringBuffer()

    fun parse(input: InputStream): Feed {
        if (::feed.isInitialized) {
            throw IllegalStateException("parse may only be called once")
        }
        val factory = SAXParserFactory.newInstance()
        val parser = factory.newSAXParser()
        parser.parse(input, this)
        return feed
    }

    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if(log.isTraceEnabled) log.trace("startElement: $qName")
        parsedString = StringBuffer()
        when(qName) {
            "feed" -> feed = Feed()
            "entry" -> {
                isParsingEntry = true
                id = ""
                title = ""
                content = ""
                summary = ""
                link = ""
                updated = Instant.MIN
                published = Instant.MIN
            }
            "link" -> {
                if(attributes != null) {
                    parsedString.append(attributes.getValue("href"))
                    rel = attributes.getValue("rel")
                }
            }
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        if(log.isTraceEnabled) log.trace("endElement: $qName")
        when(qName) {
            "entry" -> {
                feed.entries.add(Entry(id, title, link, summary, content, published, updated))
                isParsingEntry = false
            }
            "title" -> {
                if (! isParsingEntry) {
                    feed.title = getParsedString()
                } else {
                    title = getParsedString()
                }
            }
            "subtitle" -> {
                if (!isParsingEntry) {
                    feed.subtitle = getParsedString()
                }
            }
            "id" -> {
                if (!isParsingEntry) {
                    feed.id = getParsedString()
                } else {
                    id = getParsedString()
                }
            }
            "updated" -> {
                if (!isParsingEntry) {
                    feed.updated = Instant.parse(getParsedString())
                } else {
                    updated = Instant.parse(getParsedString())
                }
            }
            "published" -> published = Instant.parse(getParsedString())
            "link" -> {
                if (! isParsingEntry ) {
                    when(rel) {
                        "self" -> feed.linkSelf = getParsedString()
                        "alternate" -> feed.linkAlternate = getParsedString()
                    }
                } else {
                    link = getParsedString()
                }
            }
            "summary" -> summary = getParsedString()
            "content" -> content = getParsedString()
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