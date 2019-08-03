package li.doerf.feeder.common.repositories

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.Item
import li.doerf.feeder.common.entities.projections.ItemCount
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : CrudRepository<Item, Long> {
    fun findByFeedPkey(feedPkey: Long, pageable: Pageable): Page<Item>
    fun findByFeedPkeyAndPkeyIsLessThan(feedPkey: Long, pkey: Long, pageable: Pageable): Page<Item>
    fun findAllByFeed(feed: Feed): List<Item>
    @Query("SELECT count(i) as count, i.feed.pkey as feedPkey from Item i where i.feed.pkey in ?1 group by i.feed.pkey")
    fun countItemsGroupByFeeds(feedPkeys: Collection<Long>): List<ItemCount>
}