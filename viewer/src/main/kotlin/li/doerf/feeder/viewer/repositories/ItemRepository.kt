package li.doerf.feeder.viewer.repositories

import li.doerf.feeder.viewer.entities.Item
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : CrudRepository<Item, Long> {
    fun findTop30ByFeedPkeyOrderByPublishedDesc(feedPkey: Long): Collection<Item>
}