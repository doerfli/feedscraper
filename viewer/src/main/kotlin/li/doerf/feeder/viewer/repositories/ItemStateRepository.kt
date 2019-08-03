package li.doerf.feeder.viewer.repositories

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.Item
import li.doerf.feeder.common.entities.projections.ItemCount
import li.doerf.feeder.viewer.entities.ItemState
import li.doerf.feeder.viewer.entities.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ItemStateRepository : CrudRepository<ItemState, Long> {
    fun findByUserAndFeedAndItem(user: User, feed: Feed, item: Item): Optional<ItemState>
    fun findAllByUserAndFeed_Pkey(user: User, feedPkey: Long): List<ItemState>
    @Query("SELECT COUNT(s) AS count, s.feed.pkey AS feedPkey FROM ItemState s " +
            "WHERE s.user = ?1 AND s.feed.pkey IN ?2 AND s.isRead = true " +
            "GROUP BY s.feed.pkey")
    fun countReadItemsGroupByFeeds(user: User, feedPkeys: List<Long>): List<ItemCount>
}