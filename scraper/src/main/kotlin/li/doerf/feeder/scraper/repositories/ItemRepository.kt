package li.doerf.feeder.scraper.repositories

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.Item
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : CrudRepository<Item, Long> {
    fun findAllByFeed(feed: Feed): List<Item>
}