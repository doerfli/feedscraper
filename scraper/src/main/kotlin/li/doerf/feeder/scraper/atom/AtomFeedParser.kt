package li.doerf.feeder.scraper.atom

import li.doerf.feeder.scraper.FeedParserBase
import li.doerf.feeder.scraper.dto.EntryDto
import li.doerf.feeder.scraper.dto.FeedDto
import li.doerf.feeder.scraper.dto.FeedSourceType
import li.doerf.feeder.scraper.util.getLogger
import org.xml.sax.Attributes
import java.time.Instant


class AtomFeedParser : FeedParserBase() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    private var isParsingEntry = false

    private lateinit var id: String
    private lateinit var title: String
    private lateinit var content: String
    private lateinit var summary: String
    private lateinit var link: String
    private lateinit var updated: Instant
    private lateinit var published: Instant
    private var rel: String? = null

    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if(log.isTraceEnabled) log.trace("startElement: $qName")
        parsedString = StringBuffer()
        when(qName) {
            "feed" -> {
                log.debug("creating new atom feed object")
                feed = FeedDto(sourceType = FeedSourceType.Atom)
            }
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
                feed.entries.add(EntryDto(id, title, link, summary, content, published, updated))
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

}