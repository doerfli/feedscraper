package li.doerf.feeder.viewer.services

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.FeedType
import li.doerf.feeder.common.repositories.FeedRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant


@ExtendWith(SpringExtension::class, MockitoExtension::class)
@DataJpaTest
@Import(FeedServiceImpl::class, SimpMessagingTemplate::class)
class FeedServiceImplDataJpaTest {

    @Autowired
    private lateinit var feedRepository: FeedRepository
    @Autowired
    private lateinit var feedService: FeedServiceImpl
    @MockBean
    private lateinit var wsTemplate: SimpMessagingTemplate

    @Test
    fun testGetAll() {
        val feed1 = Feed(0, "https://www.heise.de/rss/heise-atom.xml", Instant.now(), "https://www.heise.de/rss/heise-atom.xml",
                "Heise News", "Nachrichten", Instant.now(), "https://www.heise.de/rss/heise-atom.xml", "https://www.heise.de/", FeedType.Atom)
        feedRepository.save(feed1)
        val feed2 = Feed(0, "https://www.heise.de/rss/heise-atom2.xml")
        feedRepository.save(feed2)
        val feed3 = Feed(0, "https://www.aaa.com/xml", Instant.now(), "https://www.aaa.com",
                "aaaaaHeise News", "Nachrichten", Instant.now(), "https://www.heise.de/rss/heise-atom.xml", "https://www.heise.de/", FeedType.Atom)
        feedRepository.save(feed3)

        val feeds = feedService.getAllActiveFeeds()

        assertThat(feeds.size).isEqualTo(2)
        val fa = feeds.toTypedArray()
        assertThat(fa[0].title).isEqualTo("aaaaaHeise News")
        assertThat(fa[1].title).isEqualTo("Heise News")
    }

    @Test
    fun testAdd() {
        val url = "http://www.heise.de/feeds"
        val feed = feedService.add(url)

        assertThat(feed.url).isEqualTo(url)

        val feedEnt = feedRepository.findFeedByUrl(feed.url)
        assertThat(feedEnt.isPresent)
    }

    @Test
    fun testAddExists() {
        val feed1 = Feed(0, "http://www.heise.de/feeds", Instant.now(), "https://www.heise.de/rss/heise-atom.xml",
                "Heise News", "Nachrichten", Instant.now(), "https://www.heise.de/rss/heise-atom.xml", "https://www.heise.de/", FeedType.Atom)
        feedRepository.save(feed1)

        val url = "http://www.heise.de/feeds"

        assertThatThrownBy { feedService.add(url) }.isInstanceOf(IllegalArgumentException::class.java)
    }

}