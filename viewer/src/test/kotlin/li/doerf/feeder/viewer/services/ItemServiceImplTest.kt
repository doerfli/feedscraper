package li.doerf.feeder.viewer.services

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.FeedType
import li.doerf.feeder.common.entities.Item
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.repositories.ItemRepository
import li.doerf.feeder.viewer.entities.AccountState
import li.doerf.feeder.viewer.entities.Role
import li.doerf.feeder.viewer.entities.User
import li.doerf.feeder.viewer.repositories.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.time.temporal.ChronoUnit

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@Import(ItemServiceImpl::class)
@DataJpaTest
@DirtiesContext
class ItemServiceImplTest {

    @Autowired
    private lateinit var serviceImpl: ItemServiceImpl
    @Autowired
    private lateinit var feedRepository: FeedRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Test
    fun testGetItemsByFeed() {
        // GIVEN
        val user = createUser()
        val feed = createFeed()
        val cItems = createItems(feed, 35)

        // WHEN
        val items = serviceImpl.getItemsByFeed(feed.pkey, user, null, 30)

        // THEN
        assertThat(items.size).isEqualTo(30)

        val first = items.first()
        assertThat(first.title).isEqualTo("title 35")

        val last = items.last()
        assertThat(last.title).isEqualTo("title 6")
    }

    @Test
    fun testGetItemsByFeed2() {
        // GIVEN
        val user = createUser()
        val feed = createFeed()
        val gItems = createItems(feed, 100)

        // get first 30
        var items = serviceImpl.getItemsByFeed(feed.pkey, user, null, 30)

        // WHEN - getting 2nd batch of 30
        items = serviceImpl.getItemsByFeed(feed.pkey, user, items[29].pkey, 30)

        // THEN
        assertThat(items.size).isEqualTo(30)

        var first = items.first()
        assertThat(first.title).isEqualTo("title 70")

        var last = items.last()
        assertThat(last.title).isEqualTo("title 41")

        // WHEN - getting third batch of 30
        items = serviceImpl.getItemsByFeed(feed.pkey, user, items[29].pkey, 30)

        // THEN
        assertThat(items.size).isEqualTo(30)

        first = items.first()
        assertThat(first.title).isEqualTo("title 40")

        last = items.last()
        assertThat(last.title).isEqualTo("title 11")

        // WHEN - getting fourth batch of 30 - only 10 remaining
        items = serviceImpl.getItemsByFeed(feed.pkey, user, items[29].pkey, 30)

        // THEN
        assertThat(items.size).isEqualTo(10)

        // WHEN - getting fifth batch of 30 - none remaining
        items = serviceImpl.getItemsByFeed(feed.pkey, user, items[9].pkey, 30)

        // THEN
        assertThat(items.size).isEqualTo(0)
    }

    private fun createUser(): User {
        val user = User(0, "someone@test.com", "aaaaaaaa", mutableListOf(Role.ROLE_CLIENT),
                null, null, AccountState.Confirmed)
        userRepository.save(user)
        return user
    }

    private fun createFeed(): Feed {
        val feed1 = Feed(0, "https://www.heise.de/rss/heise-atom.xml", Instant.now(), "https://www.heise.de/rss/heise-atom.xml",
                "Heise News", "Nachrichten", Instant.now(), "https://www.heise.de/rss/heise-atom.xml", "https://www.heise.de/", FeedType.Atom)
        feedRepository.save(feed1)
        return feed1
    }

    private fun createItems(feed: Feed, num: Int): List<Item> {
        val now = Instant.now()
        val items = mutableListOf<Item>()
        for(i in 1 .. num) {
            val time = now.minus(num.toLong(), ChronoUnit.MINUTES).plus(i.toLong(), ChronoUnit.MINUTES)
            val item = Item(0, feed, "id_$i", "title $i", "http://www.link.com/$i",
                    "summary $i", "content $i",
                    time, time)
            itemRepository.save(item)
            items.add(item)
        }
        return items
    }

}