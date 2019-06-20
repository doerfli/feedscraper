package li.doerf.feeder.scraper

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.FeedType
import li.doerf.feeder.common.entities.Item
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.repositories.ItemRepository
import li.doerf.feeder.scraper.dto.FeedDto
import li.doerf.feeder.scraper.dto.ItemDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.time.Instant
import java.time.temporal.ChronoUnit


@SpringBootTest
@ExtendWith(MockitoExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FeedPersisterStepTest {

    @Autowired
    private lateinit var feedRepository: FeedRepository
    @Autowired
    private lateinit var itemRepository: ItemRepository
    @Autowired
    private lateinit var feedPersisterStep: FeedPersisterStep

    @Test
    fun testPersist__NewFeed_NewItem() {
        // given
        val uri = "http://www.someurl.com/atom.xml"
        val feed = Feed(0, uri, type = FeedType.Atom)
        feedRepository.save(feed)

        val instant1 = Instant.now()

        val feedDto = FeedDto(
                "https://www.heise.de/rss/heise-top-atom.xml",
                "heise online Top-News",
                "Nachrichten nicht nur aus der Welt der Computer",
                instant1,
                "https://www.heise.de/atom/heise-top-atom.xml",
                "https://www.heise.de"
        )
        feedDto.items.add(ItemDto(
                "http://heise.de/-4430944",
                "5G-Auktion überspringt Marke von sechs Milliarden Euro",
                "https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom",
                "Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.",
                "<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>",
                instant1,
                instant1
        ))

        // when
        feedPersisterStep.persist(uri, feedDto)

        // then
        val rFeed = feedRepository.findFeedByUrl(uri).get()
        assertThat(rFeed.id).isEqualTo("https://www.heise.de/rss/heise-top-atom.xml")
        assertThat(rFeed.title).isEqualTo("heise online Top-News")
        assertThat(rFeed.subtitle).isEqualTo("Nachrichten nicht nur aus der Welt der Computer")
        assertThat(rFeed.updated).isEqualTo(instant1)
        assertThat(rFeed.linkSelf).isEqualTo("https://www.heise.de/atom/heise-top-atom.xml")
        assertThat(rFeed.linkAlternate).isEqualTo("https://www.heise.de")
        assertThat(rFeed.lastDownloaded).isAfter(instant1)

        val rEntries = itemRepository.findAllByFeed(rFeed)
        assertThat(rEntries).isNotEmpty
        assertThat(rEntries.size).isEqualTo(1)

        val firstItem = rEntries.first()
        assertThat(firstItem.id).isEqualTo("http://heise.de/-4430944")
        assertThat(firstItem.title).isEqualTo("5G-Auktion überspringt Marke von sechs Milliarden Euro")
        assertThat(firstItem.link).isEqualTo("https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom")
        assertThat(firstItem.summary).isEqualTo("Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.")
        assertThat(firstItem.content).isEqualTo("<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>")
        assertThat(firstItem.published).isEqualTo(instant1)
        assertThat(firstItem.updated).isEqualTo(instant1)
    }

    @Test
    fun testPersist__SameFeed_NewItem() {
        // given
        val uri = "http://www.someurl.com/atom.xml"
        val fiveMinAgo = Instant.now().minus(5, ChronoUnit.MINUTES)
        val instant1 = Instant.now()
        val feed = Feed(0,
                uri,
                fiveMinAgo,
                "https://www.heise.de/rss/heise-top-atom.xml",
                "heise online Top-News",
                "Nachrichten nicht nur aus der Welt der Computer",
                fiveMinAgo,
                "https://www.heise.de/atom/heise-top-atom.xml",
                "https://www.heise.de",
                FeedType.Atom)
        feedRepository.save(feed)


        val feedDto = FeedDto(
                "https://www.heise.de/rss/heise-top-atom.xml",
                "heise online Top-News",
                "Nachrichten nicht nur aus der Welt der Computer",
                instant1,
                "https://www.heise.de/atom/heise-top-atom.xml",
                "https://www.heise.de"
        )
        feedDto.items.add(ItemDto(
                "http://heise.de/-4430944",
                "5G-Auktion überspringt Marke von sechs Milliarden Euro",
                "https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom",
                "Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.",
                "<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>",
                instant1,
                instant1
        ))

        // when
        feedPersisterStep.persist(uri, feedDto)

        // then
        val rFeed = feedRepository.findFeedByUrl(uri).get()
        assertThat(rFeed.id).isEqualTo("https://www.heise.de/rss/heise-top-atom.xml")
        assertThat(rFeed.updated).isEqualTo(instant1)
        assertThat(rFeed.lastDownloaded).isAfter(instant1)

        val rEntries = itemRepository.findAllByFeed(rFeed)
        assertThat(rEntries).isNotEmpty
        assertThat(rEntries.size).isEqualTo(1)

        val firstItem = rEntries.first()
        assertThat(firstItem.id).isEqualTo("http://heise.de/-4430944")
        assertThat(firstItem.title).isEqualTo("5G-Auktion überspringt Marke von sechs Milliarden Euro")
        assertThat(firstItem.link).isEqualTo("https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom")
        assertThat(firstItem.summary).isEqualTo("Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.")
        assertThat(firstItem.content).isEqualTo("<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>")
        assertThat(firstItem.published).isEqualTo(instant1)
        assertThat(firstItem.updated).isEqualTo(instant1)
    }

    @Test
    fun testPersist__SameFeed() {
        // given
        val uri = "http://www.someurl.com/atom.xml"
        val fiveMinAgo = Instant.now().minus(5, ChronoUnit.MINUTES)
        val instant1 = Instant.now()
        val feed = Feed(0,
                uri,
                fiveMinAgo,
                "https://www.heise.de/rss/heise-top-atom.xml",
                "heise online Top-News",
                "Nachrichten nicht nur aus der Welt der Computer",
                fiveMinAgo,
                "https://www.heise.de/atom/heise-top-atom.xml",
                "https://www.heise.de",
                FeedType.Atom)
        feedRepository.save(feed)


        val feedDto = FeedDto(
                "https://www.heise.de/rss/heise-top-atom.xml",
                "heise online Top-News",
                "Nachrichten nicht nur aus der Welt der Computer",
                fiveMinAgo,
                "https://www.heise.de/atom/heise-top-atom.xml",
                "https://www.heise.de"
        )
        feedDto.items.add(ItemDto(
                "http://heise.de/-4430944",
                "5G-Auktion überspringt Marke von sechs Milliarden Euro",
                "https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom",
                "Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.",
                "<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>",
                instant1,
                instant1
        ))

        // when
        feedPersisterStep.persist(uri, feedDto)

        // then
        val rFeed = feedRepository.findFeedByUrl(uri).get()
        assertThat(rFeed.id).isEqualTo("https://www.heise.de/rss/heise-top-atom.xml")
        assertThat(rFeed.updated).isEqualTo(fiveMinAgo)
        assertThat(rFeed.lastDownloaded).isAfter(instant1)

        val rEntries = itemRepository.findAllByFeed(rFeed)
        assertThat(rEntries).isEmpty()

    }

    @Test
    fun testPersist__SameFeed_SameItem() {
        // given
        val uri = "http://www.someurl.com/atom.xml"
        val fiveMinAgo = Instant.now().minus(5, ChronoUnit.MINUTES)
        val instant1 = Instant.now()
        val feed = Feed(0,
                uri,
                fiveMinAgo,
                "https://www.heise.de/rss/heise-top-atom.xml",
                "heise online Top-News",
                "Nachrichten nicht nur aus der Welt der Computer",
                fiveMinAgo,
                "https://www.heise.de/atom/heise-top-atom.xml",
                "https://www.heise.de",
                FeedType.Atom)
        feedRepository.save(feed)

        val item = Item(
                0,
                feed,
                "http://heise.de/-4430944",
                "5G-Auktion überspringt Marke von sechs Milliarden Euro",
                "https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom",
                "Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.",
                "<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>",
                fiveMinAgo,
                fiveMinAgo
        )
        itemRepository.save(item)


        val feedDto = FeedDto(
                "https://www.heise.de/rss/heise-top-atom.xml",
                "heise online Top-News",
                "Nachrichten nicht nur aus der Welt der Computer",
                fiveMinAgo,
                "https://www.heise.de/atom/heise-top-atom.xml",
                "https://www.heise.de"
        )
        feedDto.items.add(ItemDto(
                "http://heise.de/-4430944",
                "5G-Auktion überspringt Marke von sechs Milliarden Euro",
                "https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom",
                "Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.",
                "<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>",
                fiveMinAgo,
                fiveMinAgo
        ))

        // when
        feedPersisterStep.persist(uri, feedDto)

        // then
        val rFeed = feedRepository.findFeedByUrl(uri).get()
        assertThat(rFeed.id).isEqualTo("https://www.heise.de/rss/heise-top-atom.xml")
        assertThat(rFeed.updated).isEqualTo(fiveMinAgo)
        assertThat(rFeed.lastDownloaded).isAfter(instant1)

        val rEntries = itemRepository.findAllByFeed(rFeed)
        assertThat(rEntries).isNotEmpty
        assertThat(rEntries.size).isEqualTo(1)

        val firstItem = rEntries.first()
        assertThat(firstItem.id).isEqualTo("http://heise.de/-4430944")
        assertThat(firstItem.title).isEqualTo("5G-Auktion überspringt Marke von sechs Milliarden Euro")
        assertThat(firstItem.link).isEqualTo("https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom")
        assertThat(firstItem.summary).isEqualTo("Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.")
        assertThat(firstItem.content).isEqualTo("<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>")
        assertThat(firstItem.published).isEqualTo(fiveMinAgo)
        assertThat(firstItem.updated).isEqualTo(fiveMinAgo)
    }

    @Test
    fun testPersist__UpdatedFeed_UnchangedItem() {
        // given
        val uri = "http://www.someurl.com/atom.xml"
        val fiveMinAgo = Instant.now().minus(5, ChronoUnit.MINUTES)
        val instant1 = Instant.now()
        val feed = Feed(0,
                uri,
                fiveMinAgo,
                "https://www.heise.de/rss/heise-top-atom.xml",
                "heise online Top-News",
                "Nachrichten nicht nur aus der Welt der Computer",
                fiveMinAgo,
                "https://www.heise.de/atom/heise-top-atom.xml",
                "https://www.heise.de",
                FeedType.Atom)
        feedRepository.save(feed)

        val item = Item(
                0,
                feed,
                "http://heise.de/-4430944",
                "5G-Auktion überspringt Marke von sechs Milliarden Euro",
                "https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom",
                "Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.",
                "<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>",
                fiveMinAgo,
                fiveMinAgo
        )
        itemRepository.save(item)


        val feedDto = FeedDto(
                "https://www.heise.de/rss/heise-top-atom.xml",
                "heise online Top-News",
                "Nachrichten nicht nur aus der Welt der Computer",
                instant1,
                "https://www.heise.de/atom/heise-top-atom.xml",
                "https://www.heise.de"
        )
        feedDto.items.add(ItemDto(
                "http://heise.de/-4430944",
                "5G-Auktion überspringt Marke von sechs Milliarden Euro",
                "https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom",
                "Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.",
                "<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>",
                fiveMinAgo,
                fiveMinAgo
        ))

        // when
        feedPersisterStep.persist(uri, feedDto)

        // then
        val rFeed = feedRepository.findFeedByUrl(uri).get()
        assertThat(rFeed.id).isEqualTo("https://www.heise.de/rss/heise-top-atom.xml")
        assertThat(rFeed.updated).isEqualTo(instant1)
        assertThat(rFeed.lastDownloaded).isAfter(instant1)

        val rEntries = itemRepository.findAllByFeed(rFeed)
        assertThat(rEntries).isNotEmpty
        assertThat(rEntries.size).isEqualTo(1)

        val firstItem = rEntries.first()
        assertThat(firstItem.id).isEqualTo("http://heise.de/-4430944")
        assertThat(firstItem.title).isEqualTo("5G-Auktion überspringt Marke von sechs Milliarden Euro")
        assertThat(firstItem.link).isEqualTo("https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom")
        assertThat(firstItem.summary).isEqualTo("Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.")
        assertThat(firstItem.content).isEqualTo("<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>")
        assertThat(firstItem.published).isEqualTo(fiveMinAgo)
        assertThat(firstItem.updated).isEqualTo(fiveMinAgo)
    }

    @Test
    fun testPersist__UpdatedFeed_UpdatedItem() {
        // given
        val uri = "http://www.someurl.com/atom.xml"
        val fiveMinAgo = Instant.now().minus(5, ChronoUnit.MINUTES)
        val instant1 = Instant.now()
        val feed = Feed(0,
                uri,
                fiveMinAgo,
                "https://www.heise.de/rss/heise-top-atom.xml",
                "heise online Top-News",
                "Nachrichten nicht nur aus der Welt der Computer",
                fiveMinAgo,
                "https://www.heise.de/atom/heise-top-atom.xml",
                "https://www.heise.de",
                FeedType.Atom)
        feedRepository.save(feed)

        val item = Item(
                0,
                feed,
                "http://heise.de/-4430944",
                "5G-Auktion überspringt Marke von sechs Milliarden Euro",
                "https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom",
                "Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.",
                "<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>",
                fiveMinAgo,
                fiveMinAgo
        )
        itemRepository.save(item)


        val feedDto = FeedDto(
                "https://www.heise.de/rss/heise-top-atom.xml",
                "heise online Top-News",
                "Nachrichten nicht nur aus der Welt der Computer",
                instant1,
                "https://www.heise.de/atom/heise-top-atom.xml",
                "https://www.heise.de"
        )
        feedDto.items.add(ItemDto(
                "http://heise.de/-4430944",
                "6G-Auktion überspringt Marke von sechs Milliarden Euro",
                "https://www.heise.de/newsticker/meldung/6G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom",
                "Bei der Versteigerung für die 6G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.",
                "<p><a href=\"https://www.heise.de/newsticker/meldung/6G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>",
                fiveMinAgo,
                instant1
        ))

        // when
        feedPersisterStep.persist(uri, feedDto)

        // then
        val rFeed = feedRepository.findFeedByUrl(uri).get()
        assertThat(rFeed.id).isEqualTo("https://www.heise.de/rss/heise-top-atom.xml")
        assertThat(rFeed.updated).isEqualTo(instant1)
        assertThat(rFeed.lastDownloaded).isAfter(instant1)

        val rEntries = itemRepository.findAllByFeed(rFeed)
        assertThat(rEntries).isNotEmpty
        assertThat(rEntries.size).isEqualTo(1)

        val firstItem = rEntries.first()
        assertThat(firstItem.id).isEqualTo("http://heise.de/-4430944")
        assertThat(firstItem.title).isEqualTo("6G-Auktion überspringt Marke von sechs Milliarden Euro")
        assertThat(firstItem.link).isEqualTo("https://www.heise.de/newsticker/meldung/6G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom")
        assertThat(firstItem.summary).isEqualTo("Bei der Versteigerung für die 6G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.")
        assertThat(firstItem.content).isEqualTo("<p><a href=\"https://www.heise.de/newsticker/meldung/6G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>")
        assertThat(firstItem.published).isEqualTo(fiveMinAgo)
        assertThat(firstItem.updated).isEqualTo(instant1)
    }

    @Test
    fun testPersist__FeedUpdatedMin() {
        // given
        val uri = "http://www.someurl.com/atom.xml"
        val feed = Feed(0, uri, type = FeedType.Atom)
        feedRepository.save(feed)

        val instant1 = Instant.now()
        val instant2 = Instant.now().minusMillis(5 * 60 * 1000)

        val feedDto = FeedDto(
                "https://www.heise.de/rss/heise-top-atom.xml",
                "heise online Top-News",
                "Nachrichten nicht nur aus der Welt der Computer",
                Instant.MIN,
                "https://www.heise.de/atom/heise-top-atom.xml",
                "https://www.heise.de"
        )
        feedDto.items.add(ItemDto(
                "http://heise.de/-4430944",
                "5G-Auktion überspringt Marke von sechs Milliarden Euro",
                "https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom",
                "Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.",
                "<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>",
                instant1,
                instant1
        ))
        feedDto.items.add(ItemDto(
                "http://heise.de/-4430944",
                "5G-Auktion überspringt Marke von sechs Milliarden Euro",
                "https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom",
                "Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.",
                "<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>",
                instant2,
                instant2
        ))

        // when
        feedPersisterStep.persist(uri, feedDto)

        // then
        val rFeed = feedRepository.findFeedByUrl(uri).get()
        assertThat(rFeed.id).isEqualTo("https://www.heise.de/rss/heise-top-atom.xml")
        assertThat(rFeed.title).isEqualTo("heise online Top-News")
        assertThat(rFeed.subtitle).isEqualTo("Nachrichten nicht nur aus der Welt der Computer")
        assertThat(rFeed.updated).isEqualTo(instant1)
        assertThat(rFeed.linkSelf).isEqualTo("https://www.heise.de/atom/heise-top-atom.xml")
        assertThat(rFeed.linkAlternate).isEqualTo("https://www.heise.de")
        assertThat(rFeed.lastDownloaded).isAfter(instant1)

        val rEntries = itemRepository.findAllByFeed(rFeed)
        assertThat(rEntries).isNotEmpty
        assertThat(rEntries.size).isEqualTo(2)

        val firstItem = rEntries.first()
        assertThat(firstItem.id).isEqualTo("http://heise.de/-4430944")
        assertThat(firstItem.title).isEqualTo("5G-Auktion überspringt Marke von sechs Milliarden Euro")
        assertThat(firstItem.link).isEqualTo("https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html?wt_mc=rss.ho.top-news.atom")
        assertThat(firstItem.summary).isEqualTo("Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.")
        assertThat(firstItem.content).isEqualTo("<p><a href=\"https://www.heise.de/newsticker/meldung/5G-Auktion-ueberspringt-Marke-von-sechs-Milliarden-Euro-4430944.html\"><img src=\"https://www.heise.de/scale/geometry/450/q80//imgs/18/2/6/8/4/6/9/2/04-Geschichte-5G-64b00fa203ba31da.jpeg\" class=\"webfeedsFeaturedVisual\" alt=\"\" /></a></p><p>Bei der Versteigerung für die 5G-Frequenzen sind mehr als sechs Milliarden Euro zusammengekommen. Ein Ende der ermüdenden Versteigerung ist nicht in Sicht.</p>")
        assertThat(firstItem.published).isEqualTo(instant1)
        assertThat(firstItem.updated).isEqualTo(instant1)
    }

}