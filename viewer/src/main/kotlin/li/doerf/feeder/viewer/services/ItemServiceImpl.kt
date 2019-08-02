package li.doerf.feeder.viewer.services

import li.doerf.feeder.common.entities.Item
import li.doerf.feeder.common.repositories.ItemRepository
import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.dto.ItemDto
import li.doerf.feeder.viewer.dto.toDto
import li.doerf.feeder.viewer.entities.ItemState
import li.doerf.feeder.viewer.entities.User
import li.doerf.feeder.viewer.exception.HttpException
import li.doerf.feeder.viewer.repositories.ItemStateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class ItemServiceImpl @Autowired constructor(
        private val itemRepository: ItemRepository,
        private val itemStateRepository: ItemStateRepository
        ) : ItemService {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    override fun getItemsByFeed(feedId: Long, user: User, fromPkey: Long?, size: Int): List<ItemDto> {
        val items = if (fromPkey == null ) {
            itemRepository.findByFeedPkey(feedId, PageRequest.of(0, size, Sort.Direction.DESC, "published"))
        } else {
            itemRepository.findByFeedPkeyAndPkeyIsLessThan(feedId, fromPkey, PageRequest.of(0, size, Sort.Direction.DESC, "published"))
        }
        val itemReadMap = itemStateRepository.findAllByUserAndFeed_Pkey(user, feedId).map { it.item.id to it.isRead }.toMap()
        return items.get().map { it.toDto(itemReadMap.getOrDefault(it.id, false)) }.collect(Collectors.toList())
    }

    override fun markAsRead(itemId: Long, user: User): Item {
        val item = itemRepository.findById(itemId).orElseThrow { HttpException("Unknown itemId $itemId", HttpStatus.BAD_REQUEST) }
        log.debug("marking item as read $item")
        val stateOp = itemStateRepository.findByUserAndFeedAndItem(user, item.feed, item)
        if (stateOp.isEmpty) {
            val newState = ItemState(0, user, item, item.feed, true)
            itemStateRepository.save(newState)
        } else {
            val state = stateOp.get()
            state.isRead = true
            itemStateRepository.save(state)
        }
        return item
    }

    override fun markAsUnread(itemId: Long, user: User): Item {
        val item = itemRepository.findById(itemId).orElseThrow { HttpException("Unknown itemId $itemId", HttpStatus.BAD_REQUEST) }
        log.debug("marking item as unread $item")
        val stateOp = itemStateRepository.findByUserAndFeedAndItem(user, item.feed, item)
        if (stateOp.isPresent) {
            val state = stateOp.get()
            state.isRead = false
            itemStateRepository.save(state)
        }
        return item
    }

    override fun getItemsCount(feedPkeys: List<Long>): Map<Long, Long> {
        return itemRepository.countItemsGroupByFeeds(feedPkeys).map{ it.feedPkey to it.count}.toMap()
    }

}