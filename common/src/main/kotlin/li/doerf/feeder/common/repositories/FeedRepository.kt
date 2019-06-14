package li.doerf.feeder.common.repositories

import li.doerf.feeder.common.entities.Feed
import org.springframework.data.domain.Sort
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FeedRepository : CrudRepository<Feed, Long> {
    fun findAllByTitleNotNull(sort: Sort): List<Feed>
    fun countFeedsByUrl(url: String): Int
    fun countFeedsByUrlAndTitleNotNull(url: String): Int
    fun findFeedByUrl(uri: String): Optional<Feed>
}