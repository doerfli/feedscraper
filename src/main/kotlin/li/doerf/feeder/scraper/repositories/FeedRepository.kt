package li.doerf.feeder.scraper.repositories

import li.doerf.feeder.scraper.entities.Feed
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository : CrudRepository<Feed, String> {

    fun countFeedsByUrl(url: String): Int

}