package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.FeedType
import li.doerf.feeder.common.repositories.FeedRepository
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FeedsControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var feedRepository: FeedRepository

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

        // when
        mvc.perform(get("/api/feeds").contentType(MediaType.APPLICATION_JSON))

        // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(2)))
                .andExpect(jsonPath("$[0].title", `is`("aaaaaHeise News")))
                .andExpect(jsonPath("$[1].title", `is`("Heise News")))
    }

}