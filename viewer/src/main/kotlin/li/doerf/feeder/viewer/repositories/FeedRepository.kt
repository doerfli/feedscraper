package li.doerf.feeder.viewer.repositories

import li.doerf.feeder.viewer.entities.Feed
import org.springframework.data.domain.Sort
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository : CrudRepository<Feed, Long> {

    fun findAllByTitleNotNull(sort: Sort): List<Feed>

}