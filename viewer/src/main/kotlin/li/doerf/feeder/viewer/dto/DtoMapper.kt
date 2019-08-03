package li.doerf.feeder.viewer.dto

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.Item

fun Feed.toDto(unreadCount: Long): FeedDto {
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
            type!!,
            unreadCount)
}

fun Item.toDto(isRead: Boolean = false): ItemDto {
    return ItemDto(
            pkey,
            feed.pkey,
            id,
            title,
            link,
            summary,
            content,
            published,
            updated,
            isRead
    )
}