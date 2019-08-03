package li.doerf.feeder.viewer.test

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.Item
import li.doerf.feeder.common.repositories.ItemRepository
import li.doerf.feeder.viewer.entities.AccountState
import li.doerf.feeder.viewer.entities.ItemState
import li.doerf.feeder.viewer.entities.User
import li.doerf.feeder.viewer.repositories.ItemStateRepository
import li.doerf.feeder.viewer.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class TestHelper @Autowired constructor(
        val userRepository: UserRepository,
        val itemStateRepository: ItemStateRepository,
        val itemRepository: ItemRepository
){
    fun createUser( username: String): User {
        val user = User(0, username, "password", mutableListOf(),  null, null, AccountState.Confirmed)
        userRepository.save(user)
        return user
    }

    fun markItemAsRead(user: User, feed: Feed, vararg items: Item) {
        items.forEach {
            val itemState = ItemState(0, user, it, feed, true)
            itemStateRepository.save(itemState)
        }
    }

    fun createItems(feed: Feed, num: Int): List<Item> {
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