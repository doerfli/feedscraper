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
        @Column(length = 1024)
        val id: String,
        @Column(length = 1024)
        var title: String,
        @Column(length = 1024)
        var link: String,
        @Column(length = 4096)
        var summary: String,
        @Column(length = 4096)
        var content: String?,
        var published: Instant,
        var updated: Instant
        )