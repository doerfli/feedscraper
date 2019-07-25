package li.doerf.feeder.common.repositories

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : CrudRepository<Item, Long> {
    fun findByFeedPkey(feedPkey: Long, pageable: Pageable): Page<Item>
    fun findByFeedPkeyAndPkeyIsGreaterThan(feedPkey: Long, pkey: Long, pageable: Pageable): Page<Item>
    fun findAllByFeed(feed: Feed): List<Item>
}