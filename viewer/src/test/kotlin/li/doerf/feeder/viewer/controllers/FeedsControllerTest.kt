package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.FeedType
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.viewer.entities.User
import li.doerf.feeder.viewer.test.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension::class)
class FeedsControllerTest {

    private lateinit var testuser: User
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var feedRepository: FeedRepository
    @MockBean
    private lateinit var wsTemplate: SimpMessagingTemplate
    @Autowired
    private lateinit var testHelper: TestHelper

    @BeforeEach
    fun setup() {
        testuser = testHelper.createUser("test@test123.com")
    }

    @Test
    fun testIndexMethodIsSecured() {
        // when
        mvc.perform(get("/api/feeds").contentType(MediaType.APPLICATION_JSON))

        // then
                .andDo(print())
                .andExpect(status().isForbidden)
    }

    @WithMockUser(username="test@test123.com")
    @Test
    fun testIndex() {
        // given
        val feed1 = Feed(0, "https://www.heise.de/rss/heise-atom.xml", Instant.now(), "https://www.heise.de/rss/heise-atom.xml",
                "Heise News", "Nachrichten", Instant.now(), "https://www.heise.de/rss/heise-atom.xml", "https://www.heise.de/", FeedType.Atom)
        feedRepository.save(feed1)
        val feed2 = Feed(0, "https://www.heise.de/rss/heise-atom2.xml")
        feedRepository.save(feed2)
        val feed3 = Feed(0, "https://www.aaa.com/xml", Instant.now(), "https://www.aaa.com",
                "aaaaaHeise News", "Nachrichten", Instant.now(), "https://www.heise.de/rss/heise-atom.xml", "https://www.heise.de/", FeedType.Atom)
        feedRepository.save(feed3)

        val items3 = testHelper.createItems(feed1, 23)
        testHelper.markItemAsRead(testuser, feed1, items3[1], items3[2], items3[3])

        // when
        mvc.perform(get("/api/feeds").contentType(MediaType.APPLICATION_JSON))

        // then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", hasSize<Any>(2)))
                .andExpect(jsonPath("$[0].title", `is`("aaaaaHeise News")))
                .andExpect(jsonPath("$[0].unreadItems", `is`(0)))
                .andExpect(jsonPath("$[1].title", `is`("Heise News")))
                .andExpect(jsonPath("$[1].unreadItems", `is`(20)))
    }

    @WithMockUser(username="test@test123.com")
    @Test
    fun testShow() {
        // given
        val feed1 = Feed(0, "https://www.heise.de/rss/heise-atom.xml", Instant.now(), "https://www.heise.de/rss/heise-atom.xml",
                "Heise News", "Nachrichten", Instant.now(), "https://www.heise.de/rss/heise-atom.xml", "https://www.heise.de/", FeedType.Atom)
        feedRepository.save(feed1)
        val feed2 = Feed(0, "https://www.heise.de/rss/heise-atom2.xml")
        feedRepository.save(feed2)

        val items3 = testHelper.createItems(feed1, 23)
        testHelper.markItemAsRead(testuser, feed1, items3[1], items3[2], items3[3])

        // when
        mvc.perform(get("/api/feeds/${feed1.pkey}").contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.title", `is`("Heise News")))
                .andExpect(jsonPath("$.unreadItems", `is`(20)))
    }

    @WithMockUser(username="test@test123.com")
    @Test
    fun testAdd() {
        // given
        val url = "http://www.heise.de/feed/rss.xml";
        // when
        mvc.perform(post("/api/feeds").content(
                """
                    {
                        "url":"$url"
                    }
                """).contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(print())
                .andExpect(status().isOk)

        val feed = feedRepository.findFeedByUrl(url).get()
        assertThat(feed).isNotNull

        // now set title
        feed.title = "someblabla"
        feedRepository.save(feed)

        // and wait some time
        Thread.sleep(2500)
    }

    @WithMockUser(username="test@test123.com")
    @Test
    fun testAddExists() {
        // given
        val url = "http://www.heise.de/feed/rss.xml";
        val feed1 = Feed(0, url, Instant.now(), "https://www.heise.de/rss/heise-atom.xml",
                "Heise News", "Nachrichten", Instant.now(), "https://www.heise.de/rss/heise-atom.xml", "https://www.heise.de/", FeedType.Atom)
        feedRepository.save(feed1)

        // when
        mvc.perform(post("/api/feeds").content(
                """
                    {
                        "url":"$url"
                    }
                """).contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

}