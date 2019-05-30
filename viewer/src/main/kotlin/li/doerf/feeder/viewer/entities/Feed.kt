package li.doerf.feeder.viewer.entities

import java.time.Instant
import javax.persistence.*

@Entity
data class Feed (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val pkey: Long,
        // TODO unique
        val url: String,
        var lastDownloaded: Instant? = null,
        var id: String? = null,
        var title: String? = null,
        var subtitle: String? = null,
        var updated: Instant? = null,
        var linkSelf: String? = null,
        var linkAlternate: String? = null,
        @Enumerated(EnumType.STRING)
        @Column(length = 64)
        var type: FeedType? = null
)
