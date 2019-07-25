package li.doerf.feeder.viewer.services

import li.doerf.feeder.common.entities.Item
import li.doerf.feeder.viewer.dto.ItemDto
import li.doerf.feeder.viewer.entities.User

interface ItemService {

    fun getItemsByFeed(feedId: Long, user: User, fromPkey: Long?, size: Int): List<ItemDto>

    fun markAsRead(itemId: Long, user: User): Item
    fun markAsUnread(itemId: Long, user: User): Item
}