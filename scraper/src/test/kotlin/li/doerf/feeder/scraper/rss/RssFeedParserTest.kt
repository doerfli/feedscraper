package li.doerf.feeder.scraper.rss

import li.doerf.feeder.common.entities.FeedType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.time.Instant

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
        assertThat(feed.type).isEqualTo(FeedType.RSS)
        assertThat(feed.updated).isEqualTo(dateParser.parse("Fri, 24 May 2019 22:01:28 +0200").toInstant())


        Assertions.assertThat(feed.items.size).isEqualTo(30)

        val firstEntry = feed.items.first()
        assertThat(firstEntry.id).isEqualTo("https://www.spiegel.de/kultur/literatur/rezension-zu-carolin-emcke-ja-heisst-ja-und-eine-von-allen-a-1268928.html")
        assertThat(firstEntry.title).isEqualTo("Carolin Emcke über #MeToo: Eine von allen")
        assertThat(firstEntry.link).isEqualTo("https://www.spiegel.de/kultur/literatur/rezension-zu-carolin-emcke-ja-heisst-ja-und-eine-von-allen-a-1268928.html#ref=rss")
        assertThat(firstEntry.summary).isEqualTo("Warum denken wir, dass eine Frau auf dem Hotelzimmer ihres Kollegen mit dem Schlimmsten rechnen muss? Carolin Emcke spricht in ihrem Buch über #MeToo an, was sonst ausgespart wird - und schont auch sich selbst dabei nicht.")
        assertThat(firstEntry.content).isEqualTo("<img src=\"https://www.spiegel.de/images/image-1349990-thumbsmall-uojg.jpg\" hspace=\"5\" align=\"left\">Warum denken wir, dass eine Frau auf dem Hotelzimmer ihres Kollegen mit dem Schlimmsten rechnen muss? Carolin Emcke spricht in ihrem Buch über #MeToo an, was sonst ausgespart wird - und schont auch sich selbst dabei nicht.")
        assertThat(firstEntry.updated).isEqualTo(dateParser.parse("Fri, 24 May 2019 20:34:00 +0200").toInstant())

        val lastEntry = feed.items.last()
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

    @Test
    fun testParseGizmodo() {
        val inputStream = this.javaClass.getResourceAsStream("rss_gizmodo.xml")
        val parser = RssFeedParser()
        val feed = parser.parse(inputStream)
        val dateParser = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z")

//        assertThat(feed.id).isEqualTo("")
        assertThat(feed.title).isEqualTo("Gizmodo")
        assertThat(feed.linkAlternate).isEqualTo("https://gizmodo.com")
        assertThat(feed.type).isEqualTo(FeedType.RSS)
        assertThat(feed.updated).isEqualTo(Instant.MIN)


        Assertions.assertThat(feed.items.size).isEqualTo(25)

        val firstEntry = feed.items.first()
        assertThat(firstEntry.id).isEqualTo("1835692877")
        assertThat(firstEntry.title).isEqualTo("This Deal Has Our Stamp of Approval")
        assertThat(firstEntry.link).isEqualTo("https://kinjadeals.theinventory.com/this-deal-has-our-stamp-of-approval-1835692877")
        assertThat(firstEntry.summary).isEqualTo("<img src=\"https://i.kinja-img.com/gawker-media/image/upload/s--se3ymcQA--/c_fit,fl_progressive,q_80,w_636/gq2tc1trwcpzhonpznb7.jpg\" /><p><a rel=\"nofollow\" data-amazonasin=\"B07L25SDY7\" data-amazonsubtag=\"[t|link[p|1835692877[a|B07L25SDY7[au|5876237249238321302[b|gizmodo[lt|text\" onclick=\"window.ga(&#39;send&#39;, &#39;event&#39;, &#39;Commerce&#39;, &#39;gizmodo - This Deal Has Our Stamp of Approval&#39;, &#39;B07L25SDY7&#39;);window.ga(&#39;unique.send&#39;, &#39;event&#39;, &#39;Commerce&#39;, &#39;gizmodo - This Deal Has Our Stamp of Approval&#39;, &#39;B07L25SDY7&#39;);\" data-amazontag=\"gizmodoamzn-20\" href=\"https://www.amazon.com/gp/product/B07L25SDY7/ref=ox_sc_act_title_1?smid=A2B7OX7TNIXZ9S&amp;psc=1&amp;tag=gizmodoamzn-20&amp;ascsubtag=a72070c466c2731ad6a4fb71ebf1530327020a6f\">USPS US Flag 2018 Forever Stamps (Book of 40)</a> | \$22 | Amazon | Clip the 10% off coupon</p><p><a href=\"https://kinjadeals.theinventory.com/this-deal-has-our-stamp-of-approval-1835692877\">Read more...</a></p>")
        assertThat(firstEntry.content).isEqualTo("")
        assertThat(firstEntry.updated).isEqualTo(dateParser.parse("Thu, 20 Jun 2019 15:45:00 GMT").toInstant())
    }

}