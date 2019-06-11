package li.doerf.feeder.viewer.dto

import java.time.Instant

data class ItemDto (
        val pkey: Long,
        val feedPkey: Long,
        val id: String,
        var title: String,
        var link: String,
        var summary: String,
        var content: String?,
        var published: Instant,
        var updated: Instant,
        var read: Boolean
        )