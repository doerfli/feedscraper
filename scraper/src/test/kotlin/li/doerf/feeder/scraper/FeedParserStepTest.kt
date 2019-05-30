package li.doerf.feeder.scraper

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.FeedType
import li.doerf.feeder.common.repositories.FeedRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.io.BufferedReader

@SpringBootTest
@ExtendWith(MockitoExtension::class)
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
        val feedDto = feedParserStep.parse(uri, feedAsString)

        // then
        assertThat(feedDto.title).isNotNull()
        assertThat(feedDto.items).isNotEmpty
    }

    @Test
    fun testParseRssDetermined() {
        // given
        val uri = "http://www.rssurl.com/rss"
        val feed = Feed(0, uri)
        feedRepository.save(feed)
        val feedAsString = this.javaClass.getResourceAsStream("rss/rss_spiegel.xml").bufferedReader().use(BufferedReader::readText)

        // when
        val feedDto = feedParserStep.parse(uri, feedAsString)

        // then
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
        val feedDto = feedParserStep.parse(uri, feedAsString)

        // then
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
        val feedDto = feedParserStep.parse(uri, feedAsString)

        // then
        assertThat(feedDto.title).isNotNull()
        assertThat(feedDto.type).isEqualTo(FeedType.Atom)
        assertThat(feedDto.items).isNotEmpty

        val feedAfter = feedRepository.findById(feed.pkey)
        assertThat(feedAfter.get().type).isEqualTo(FeedType.Atom)
    }
}