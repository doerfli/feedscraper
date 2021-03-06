package li.doerf.feeder.scraper

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.FeedType
import li.doerf.feeder.common.repositories.FeedRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.io.BufferedReader

//@SpringBootTest
@ExtendWith(SpringExtension::class, MockitoExtension::class)
@Import(FeedParserStep::class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FeedParserStepTest {

    @Autowired
    private lateinit var feedRepository: FeedRepository

    @Autowired
    private lateinit var feedParserStep: FeedParserStep

    @Test
    fun testParseRss() {
        // given
        val uri = "http://www.rssurl.com/rss"
        val feed = Feed(0, uri, type = FeedType.RSS)
        feedRepository.save(feed)
        val feedAsString = this.javaClass.getResourceAsStream("rss/rss_spiegel.xml").bufferedReader().use(BufferedReader::readText)

        // when
        val result = feedParserStep.parse(uri, feedAsString) as ParserSuccess

        // then
        assertThat(result.feedDto.title).isNotNull()
        assertThat(result.feedDto.items).isNotEmpty
    }

    @Test
    fun testParseRssDetermined() {
        // given
        val uri = "http://www.rssurl.com/rss"
        val feed = Feed(0, uri)
        feedRepository.save(feed)
        val feedAsString = this.javaClass.getResourceAsStream("rss/rss_spiegel.xml").bufferedReader().use(BufferedReader::readText)

        // when
        val result = feedParserStep.parse(uri, feedAsString) as ParserSuccess

        // then
        val feedDto = result.feedDto
        assertThat(feedDto.title).isNotNull()
        assertThat(feedDto.type).isEqualTo(FeedType.RSS)
        assertThat(feedDto.items).isNotEmpty

        val feedAfter = feedRepository.findById(feed.pkey)
        assertThat(feedAfter.get().type).isEqualTo(FeedType.RSS)
    }

    @Test
    fun testParseAtom() {
        // given
        val uri = "http://www.rssurl.com/rss"
        val feed = Feed(0, uri, type = FeedType.Atom)
        feedRepository.save(feed)
        val feedAsString = this.javaClass.getResourceAsStream("atom/atom_heise.xml").bufferedReader().use(BufferedReader::readText)

        // when
        val result = feedParserStep.parse(uri, feedAsString) as ParserSuccess

        // then
        val feedDto = result.feedDto
        assertThat(feedDto.title).isNotNull()
        assertThat(feedDto.items).isNotEmpty
    }

    @Test
    fun testParseAtomDetermined() {
        // given
        val uri = "http://www.rssurl.com/rss"
        val feed = Feed(0, uri)
        feedRepository.save(feed)
        val feedAsString = this.javaClass.getResourceAsStream("atom/atom_heise.xml").bufferedReader().use(BufferedReader::readText)

        // when
        val result = feedParserStep.parse(uri, feedAsString)

        // then
        val feedDto = (result as ParserSuccess).feedDto
        assertThat(feedDto.title).isNotNull()
        assertThat(feedDto.type).isEqualTo(FeedType.Atom)
        assertThat(feedDto.items).isNotEmpty

        val feedAfter = feedRepository.findById(feed.pkey)
        assertThat(feedAfter.get().type).isEqualTo(FeedType.Atom)

        val firstItem = feedDto.items.first()
        assertThat(firstItem.title).isEqualTo("Wikileaks-Gründer Julian Assange zu 50 Wochen Gefängnis verurteilt")

        val item56 = feedDto.items.get(56)
        assertThat(item56.title).isEqualTo("Account-Hijacking auf Bestellung: Black-Hat-Hacker mit miesem Kundenservice")

        val item57 = feedDto.items.get(57)
        assertThat(item57.title).isEqualTo("\"Autopilot\": Verbraucherschützer und Verkehrssicherheits-Behörde stellen Tesla hart in Frage")
    }
}