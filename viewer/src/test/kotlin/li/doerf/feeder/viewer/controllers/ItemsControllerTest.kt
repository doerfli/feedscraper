package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.common.entities.*
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.repositories.ItemRepository
import li.doerf.feeder.common.repositories.ItemStateRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
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

    private lateinit var testuser: User
    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var feedRepository: FeedRepository
    @Autowired
    private lateinit var entriesRepository: ItemRepository
    @Autowired
    private lateinit var itemStateRepository: ItemStateRepository
    @Autowired
    private lateinit var testHelper: TestHelper

    @BeforeEach
    fun setup() {
        testuser = testHelper.createUser("test@test123.com")
    }

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
        createItem(feed1, "123456", "Entrytitle1", "link1", "summary1", "content1",
                sixtySAgo,
                tenSAgo,
                true)
        createItem(feed1, "123457", "Entrytitle2", "link2", "summary2", "content2",
                thirtySAgo,
                thirtySAgo)


        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/items/${feed1.pkey}").contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Any>(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", CoreMatchers.`is`("Entrytitle2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].read", CoreMatchers.`is`(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", CoreMatchers.`is`("Entrytitle1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].read", CoreMatchers.`is`(true)))

    }

    private fun createItem(feed1: Feed, id: String, title: String, link: String, summary: String, content: String, published: Instant, updated: Instant, isRead: Boolean = false): Item {
        val entry1 = Item(0, feed1, id, title, link,
                summary, content,
                published,
                updated)
        entriesRepository.save(entry1)
        val readItem = ItemState(0, testuser, entry1, feed1, isRead)
        itemStateRepository.save(readItem)
        return entry1
    }

    private fun createFeed(): Feed {
        val feed1 = Feed(0, "https://www.heise.de/rss/heise-atom.xml", Instant.now(), "https://www.heise.de/rss/heise-atom.xml",
                "Heise News", "Nachrichten", Instant.now(), "https://www.heise.de/rss/heise-atom.xml", "https://www.heise.de/", FeedType.Atom)
        feedRepository.save(feed1)
        return feed1
    }

    @WithMockUser(username="test@test123.com")
    @Test
    fun testMarkAsRead() {
        // given
        val feed1 = createFeed()

        val tenSAgo = Instant.now().minusSeconds(10)
        val sixtySAgo = Instant.now().minusSeconds(60)
        val item = createItem(feed1, "123456", "Entrytitle1", "link1", "summary1", "content1",
                sixtySAgo,
                tenSAgo)


        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/items/${feed1.pkey}/${item.pkey}/read").contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.`is`("Entrytitle1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.read", CoreMatchers.`is`(true)))
    }

    @WithMockUser(username="test@test123.com")
    @Test
    fun testMarkReadItemAsRead() {
        // given
        val feed1 = createFeed()

        val tenSAgo = Instant.now().minusSeconds(10)
        val sixtySAgo = Instant.now().minusSeconds(60)
        val item = createItem(feed1, "123456", "Entrytitle1", "link1", "summary1", "content1",
                sixtySAgo,
                tenSAgo,
                true)


        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/items/${feed1.pkey}/${item.pkey}/read").contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.`is`("Entrytitle1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.read", CoreMatchers.`is`(true)))
    }

    @WithMockUser(username="test@test123.com")
    @Test
    fun testMarkAsUnread() {
        // given
        val feed1 = createFeed()

        val tenSAgo = Instant.now().minusSeconds(10)
        val sixtySAgo = Instant.now().minusSeconds(60)
        val item = createItem(feed1, "123456", "Entrytitle1", "link1", "summary1", "content1",
                sixtySAgo,
                tenSAgo,
                true)


        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/items/${item.pkey}/unread").contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.`is`("Entrytitle1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.read", CoreMatchers.`is`(false)))
    }

    @WithMockUser(username="test@test123.com")
    @Test
    fun testMarkUnreadItemAsUnread() {
        // given
        val feed1 = createFeed()

        val tenSAgo = Instant.now().minusSeconds(10)
        val sixtySAgo = Instant.now().minusSeconds(60)
        val item = createItem(feed1, "123456", "Entrytitle1", "link1", "summary1", "content1",
                sixtySAgo,
                tenSAgo,
                false)


        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/items/${item.pkey}/unread").contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.`is`("Entrytitle1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.read", CoreMatchers.`is`(false)))
    }

}