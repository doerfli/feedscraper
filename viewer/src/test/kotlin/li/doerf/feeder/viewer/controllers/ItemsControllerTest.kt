package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.FeedType
import li.doerf.feeder.common.entities.Item
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.repositories.ItemRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemsControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var feedRepository: FeedRepository
    @Autowired
    private lateinit var entriesRepository: ItemRepository

    @Test
    fun testIndexMethodIsSecured() {
        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/items/1234").contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @WithMockUser(username="test@test123.com")
    @Test
    fun testGetAllByFeed() {
        // given
        val feed1 = createFeed()

        val tenSAgo = Instant.now().minusSeconds(10)
        val thirtySAgo = Instant.now().minusSeconds(30)
        val sixtySAgo = Instant.now().minusSeconds(60)
        createEntry(feed1, "123456", "Entrytitle1", "link1", "summary1", "content1",
                sixtySAgo,
                tenSAgo)
        createEntry(feed1, "123457", "Entrytitle2", "link2", "summary2", "content2",
                thirtySAgo,
                thirtySAgo)


        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/items/${feed1.pkey}").contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Any>(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", CoreMatchers.`is`("Entrytitle2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", CoreMatchers.`is`("Entrytitle1")))

    }

    private fun createEntry(feed1: Feed, id: String, title: String, link: String, summary: String, content: String, published: Instant, updated: Instant): Item {
        val entry1 = Item(0, feed1, id, title, link,
                summary, content,
                published,
                updated)
        entriesRepository.save(entry1)
        return entry1
    }

    private fun createFeed(): Feed {
        val feed1 = Feed(0, "https://www.heise.de/rss/heise-atom.xml", Instant.now(), "https://www.heise.de/rss/heise-atom.xml",
                "Heise News", "Nachrichten", Instant.now(), "https://www.heise.de/rss/heise-atom.xml", "https://www.heise.de/", FeedType.Atom)
        feedRepository.save(feed1)
        return feed1
    }

}