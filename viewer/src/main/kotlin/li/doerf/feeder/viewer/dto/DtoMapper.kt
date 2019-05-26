package li.doerf.feeder.viewer.dto

import li.doerf.feeder.viewer.entities.Entry
import li.doerf.feeder.viewer.entities.Feed

fun Feed.toDto(): FeedDto {
    return FeedDto(
            pkey,
            url,
            lastDownloaded!!,
            id!!,
            title!!,
            subtitle,
            updated,
            linkSelf,
            linkAlternate,
            sourceType!!)
}

fun Entry.toDto(): EntryDto {
    return EntryDto(
            pkey,
            feed.pkey,
            id,
            title,
            link,
            summary,
            content,
            published,
            updated
    )
}