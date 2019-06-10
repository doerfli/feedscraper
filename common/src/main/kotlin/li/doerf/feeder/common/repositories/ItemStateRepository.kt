package li.doerf.feeder.common.repositories

import li.doerf.feeder.common.entities.ItemState
import li.doerf.feeder.common.entities.User
import org.springframework.data.repository.CrudRepository

interface ItemStateRepository : CrudRepository<ItemState, Long> {
    fun findAllByUserAndFeed_Pkey(user: User, feedPkey: Long): List<ItemState>
}