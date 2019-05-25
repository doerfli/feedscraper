package li.doerf.feeder.scraper.entities

import li.doerf.feeder.scraper.dto.FeedSource
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Feed (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val pkey: Long,
        val url: String,
        var lastDownloaded: Instant? = null,
        var id: String? = null,
        var title: String? = null,
        var subtitle: String? = null,
        var updated: Instant? = null,
        var linkSelf: String? = null,
        var linkAlternate: String? = null,
        val source: FeedSource? = null
)
