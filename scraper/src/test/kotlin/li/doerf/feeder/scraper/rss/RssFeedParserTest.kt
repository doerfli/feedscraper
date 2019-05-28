package li.doerf.feeder.scraper.rss

import li.doerf.feeder.scraper.entities.FeedSourceType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat

class RssFeedParserTest {

    @Test
    fun testParseSpiegel() {
        val inputStream = this.javaClass.getResourceAsStream("rss_spiegel.xml")
        val parser = RssFeedParser()
        val feed = parser.parse(inputStream)
        val dateParser = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss X")

//        assertThat(feed.id).isEqualTo("")
        assertThat(feed.title).isEqualTo("SPIEGEL ONLINE - Schlagzeilen")
        assertThat(feed.linkAlternate).isEqualTo("https://www.spiegel.de")
        assertThat(feed.sourceType).isEqualTo(FeedSourceType.RSS)
        assertThat(feed.updated).isEqualTo(dateParser.parse("Fri, 24 May 2019 22:01:28 +0200").toInstant())


        Assertions.assertThat(feed.entries.size).isEqualTo(30)

        val firstEntry = feed.entries.first()
        assertThat(firstEntry.id).isEqualTo("https://www.spiegel.de/kultur/literatur/rezension-zu-carolin-emcke-ja-heisst-ja-und-eine-von-allen-a-1268928.html")
        assertThat(firstEntry.title).isEqualTo("Carolin Emcke über #MeToo: Eine von allen")
        assertThat(firstEntry.link).isEqualTo("https://www.spiegel.de/kultur/literatur/rezension-zu-carolin-emcke-ja-heisst-ja-und-eine-von-allen-a-1268928.html#ref=rss")
        assertThat(firstEntry.summary).isEqualTo("Warum denken wir, dass eine Frau auf dem Hotelzimmer ihres Kollegen mit dem Schlimmsten rechnen muss? Carolin Emcke spricht in ihrem Buch über #MeToo an, was sonst ausgespart wird - und schont auch sich selbst dabei nicht.")
        assertThat(firstEntry.content).isEqualTo("<img src=\"https://www.spiegel.de/images/image-1349990-thumbsmall-uojg.jpg\" hspace=\"5\" align=\"left\">Warum denken wir, dass eine Frau auf dem Hotelzimmer ihres Kollegen mit dem Schlimmsten rechnen muss? Carolin Emcke spricht in ihrem Buch über #MeToo an, was sonst ausgespart wird - und schont auch sich selbst dabei nicht.")
        assertThat(firstEntry.updated).isEqualTo(dateParser.parse("Fri, 24 May 2019 20:34:00 +0200").toInstant())

        val lastEntry = feed.entries.last()
        assertThat(lastEntry.id).isEqualTo("https://www.spiegel.de/wissenschaft/weltall/spacex-gelingt-start-von-60-internet-satelliten-a-1269082.html")
        assertThat(lastEntry.title).isEqualTo("Starlink-Netzwerk: SpaceX schießt 60 Internet-Satelliten ins All")
        assertThat(lastEntry.link).isEqualTo("https://www.spiegel.de/wissenschaft/weltall/spacex-gelingt-start-von-60-internet-satelliten-a-1269082.html#ref=rss")
        assertThat(lastEntry.summary).isEqualTo("Einige Unternehmen setzen auf ein Netzwerk aus Satelliten, um weltweit schnelles Internet anzubieten. Elon Musks Firma SpaceX hat jetzt beim Projekt Starlink einen entscheidenden Schritt gemacht.")
        assertThat(lastEntry.content).isEqualTo("<img src=\"https://www.spiegel.de/images/image-1431303-thumbsmall-shuf.jpg\" hspace=\"5\" align=\"left\">Einige Unternehmen setzen auf ein Netzwerk aus Satelliten, um weltweit schnelles Internet anzubieten. Elon Musks Firma SpaceX hat jetzt beim Projekt Starlink einen entscheidenden Schritt gemacht.")
        assertThat(lastEntry.published).isEqualTo(dateParser.parse("Fri, 24 May 2019 13:03:00 +0200").toInstant())
        assertThat(lastEntry.updated).isEqualTo(dateParser.parse("Fri, 24 May 2019 13:03:00 +0200").toInstant())
    }

    @Test
    fun testParserTwice() {
        val inputStream = this.javaClass.getResourceAsStream("rss_spiegel.xml")
        val parser = RssFeedParser()
        parser.parse(inputStream)
        Assertions.assertThatThrownBy {
            parser.parse(inputStream)
        }.isInstanceOf(IllegalStateException::class.java)
    }

}