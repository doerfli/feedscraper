package li.doerf.feeder.scraper.repositories

import li.doerf.feeder.scraper.entities.Entry
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EntryRepository : CrudRepository<Entry, String> {

}