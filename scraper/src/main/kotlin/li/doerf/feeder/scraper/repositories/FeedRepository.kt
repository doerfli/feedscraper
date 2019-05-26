package li.doerf.feeder.scraper.repositories

import li.doerf.feeder.scraper.entities.Feed
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FeedRepository : CrudRepository<Feed, Long> {

    fun countFeedsByUrl(url: String): Int
    fun findFeedByUrl(uri: String): Optional<Feed>

}