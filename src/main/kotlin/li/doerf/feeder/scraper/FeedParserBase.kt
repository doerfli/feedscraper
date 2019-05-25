package li.doerf.feeder.scraper

import li.doerf.feeder.scraper.dto.FeedDto
import li.doerf.feeder.scraper.util.getLogger
import org.xml.sax.helpers.DefaultHandler
import java.io.InputStream
import javax.xml.parsers.SAXParserFactory

open class FeedParserBase : DefaultHandler() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        protected val log = getLogger(javaClass)
    }

    protected lateinit var feed: FeedDto
    protected var parsedString = StringBuffer()

    fun parse(input: InputStream): FeedDto {
        if (::feed.isInitialized) {
            throw IllegalStateException("parse may only be called once")
        }
        val factory = SAXParserFactory.newInstance()
        val parser = factory.newSAXParser()
        log.debug("starting to parse feed")
        parser.parse(input, this)
        log.debug("finished parsing feed")
        return feed
    }

    protected fun getParsedString() = parsedString.toString().trim()

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        if (ch == null) {
            return
        }
        val t = ch.sliceArray(IntRange(start, start + length - 1)) // IntRange end is inclusive
        parsedString.append(t)
    }

}