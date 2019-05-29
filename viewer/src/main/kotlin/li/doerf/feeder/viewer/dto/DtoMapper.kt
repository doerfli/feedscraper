package li.doerf.feeder.viewer.dto

import li.doerf.feeder.viewer.entities.Feed
import li.doerf.feeder.viewer.entities.Item

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
            type!!)
}

fun Item.toDto(): ItemDto {
    return ItemDto(
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