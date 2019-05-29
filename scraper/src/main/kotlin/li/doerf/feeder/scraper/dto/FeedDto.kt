package li.doerf.feeder.scraper.dto

import li.doerf.feeder.scraper.entities.FeedType
import java.time.Instant

data class FeedDto (
        var id: String = "",
        var title: String = "",
        var subtitle: String = "",
        var updated: Instant = Instant.MIN,
        var linkSelf: String = "",
        var linkAlternate: String = "",
        val items: MutableList<ItemDto> = mutableListOf(),
        val type: FeedType = FeedType.Atom
)