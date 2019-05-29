package li.doerf.feeder.viewer.dto

import li.doerf.feeder.viewer.entities.FeedType
import java.time.Instant

data class FeedDto (
        val pkey: Long,
        val url: String,
        var lastDownloaded: Instant,
        val id: String,
        val title: String,
        val subtitle: String?,
        val updated: Instant?,
        val linkSelf: String?,
        val linkAlternate: String?,
        val type: FeedType
)
