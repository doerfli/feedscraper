package li.doerf.feeder.common.repositories

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.Item
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : CrudRepository<Item, Long> {
    fun findTop30ByFeedPkeyOrderByPublishedDesc(feedPkey: Long): Collection<Item>
    fun findAllByFeed(feed: Feed): List<Item>
}