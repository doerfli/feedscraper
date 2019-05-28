package li.doerf.feeder.scraper.atom

import li.doerf.feeder.scraper.entities.FeedSourceType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.Instant

class AtomFeedParserTest {

    @Test
    fun testParseHeise() {
        val inputStream = this.javaClass.getResourceAsStream("atom_heise.xml")
        val parser = AtomFeedParser()
        val feed = parser.parse(inputStream)

        assertThat(feed.id).isEqualTo("https://www.heise.de/rss/heise-top-atom.xml")
        assertThat(feed.title).isEqualTo("heise online Top-News")
        assertThat(feed.subtitle).isEqualTo("Nachrichten nicht nur aus der Welt der Computer")
        assertThat(feed.linkSelf).isEqualTo("https://www.heise.de/rss/heise-top-atom.xml")
        assertThat(feed.linkAlternate).isEqualTo("https://www.heise.de/")
        assertThat(feed.updated).isEqualTo(Instant.parse("2019-05-23T19:13:00+02:00"))
        assertThat(feed.sourceType).isEqualTo(FeedSourceType.Atom)

        assertThat(feed.entries.size).isEqualTo(60)

        val firstEntry = feed.entries.first()
        assertThat(firstEntry.id).isEqualTo("http://heise.de/-4430944")
        assertThat(firstEntry.title).isEqualTo("5G-Auktion überspringt Marke von sechs Milliarden Euro")
        assertThat(firstEntry.link).isEqualTo("https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom")
        assertThat(firstEntry.summary).isEqualTo("Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.")
        assertThat(firstEntry.content).isEqualTo("<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>")
        assertThat(firstEntry.published).isEqualTo(Instant.parse("2019-05-23T19:13:00+02:00"))
        assertThat(firstEntry.updated).isEqualTo(Instant.parse("2019-05-23T19:13:00+02:00"))

        val fourthEntry = feed.entries.get(4)
        assertThat(fourthEntry.id).isEqualTo("http://heise.de/-4425906")
        assertThat(fourthEntry.published).isEqualTo(Instant.parse("2019-05-20T17:17:00+02:00"))
        assertThat(fourthEntry.updated).isEqualTo(Instant.parse("2019-05-23T09:13:00+02:00"))

        val lastEntry = feed.entries.get(59)
        assertThat(lastEntry.id).isEqualTo("http://heise.de/-4411174")
        assertThat(lastEntry.title).isEqualTo("Wikileaks-Gründer Julian Assange zu 50 Wochen Gefängnis verurteilt")
        assertThat(lastEntry.link).isEqualTo("https://www.heise.de/newsticker/meldung/Wikileaks-Gruender-Julian-Assange-zu-50-Wochen-Gefaengnis-verurteilt-4411174.html?wt_mc=rss.ho.top-news.atom")
        assertThat(lastEntry.summary).isEqualTo("Der Verstoß gegen Kautionsauflagen bringt Julian Assange ins Gefängnis. Über das Auslieferungsersuchen der USA wird noch entschieden.")
        assertThat(lastEntry.content).isEqualTo("<p><a href=\"https://www.heise.de/newsticker/meldung/Wikileaks-Gruender-Julian-Assange-zu-50-Wochen-Gefaengnis-verurteilt-4411174.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/7/0/6/0/2/JULIAN_ASSANGE-34fb2eaf2657f3d3.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Der Verstoß gegen Kautionsauflagen bringt Julian Assange ins Gefängnis. Über das Auslieferungsersuchen der USA wird noch entschieden.</p>")
        assertThat(lastEntry.published).isEqualTo(Instant.parse("2019-05-01T13:45:00+02:00"))
        assertThat(lastEntry.updated).isEqualTo(Instant.parse("2019-05-01T13:45:00+02:00"))
    }

    @Test
    fun testParserTwice() {
        val inputStream = this.javaClass.getResourceAsStream("atom_heise.xml")
        val parser = AtomFeedParser()
        parser.parse(inputStream)
        assertThatThrownBy {
            parser.parse(inputStream)
        }.isInstanceOf(IllegalStateException::class.java)
    }

}