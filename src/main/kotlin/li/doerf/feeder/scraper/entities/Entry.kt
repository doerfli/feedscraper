package li.doerf.feeder.scraper.entities

import java.time.Instant
import javax.persistence.*

@Entity
data class Entry (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val pkey: Long,
        @ManyToOne
        @JoinColumn(name = "feed_id")
        val feed: Feed,
        val id: String,
        var title: String,
        var link: String,
        var summary: String,
        var content: String?,
        var published: Instant,
        var updated: Instant
        )