package li.doerf.feeder.viewer.repositories

import li.doerf.feeder.viewer.entities.Entry
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EntryRepository : CrudRepository<Entry, Long> {
    fun findTop30ByFeedPkeyOrderByPublishedDesc(feedPkey: Long): Collection<Entry>
}