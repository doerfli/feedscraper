package li.doerf.feeder.scraper.repositories

import li.doerf.feeder.scraper.entities.Item
import li.doerf.feeder.scraper.entities.Feed
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : CrudRepository<Item, Long> {
    fun findAllByFeed(feed: Feed): List<Item>
}