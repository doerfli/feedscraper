package li.doerf.feeder.scraper.dto

import java.time.Instant

data class FeedDto (
        var id: String = "",
        var title: String = "",
        var subtitle: String = "",
        var updated: Instant = Instant.MIN,
        var linkSelf: String = "",
        var linkAlternate: String = "",
        val entries: MutableList<EntryDto> = mutableListOf(),
        val sourceType: FeedSourceType = FeedSourceType.Atom
)