package li.doerf.feeder.scraper.repositories

import li.doerf.feeder.scraper.entities.Entry
import li.doerf.feeder.scraper.entities.Feed
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EntryRepository : CrudRepository<Entry, Long> {
    fun findAllByFeed(feed: Feed): List<Entry>
}