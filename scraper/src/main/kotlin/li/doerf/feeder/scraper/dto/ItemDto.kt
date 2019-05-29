package li.doerf.feeder.scraper.dto

import java.time.Instant

data class ItemDto (
        val id: String,
        val title: String,
        val link: String,
        val summary: String,
        val content: String,
        val published: Instant,
        val updated: Instant
)