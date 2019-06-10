package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.common.repositories.ItemRepository
import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.HttpException
import li.doerf.feeder.viewer.dto.ItemDto
import li.doerf.feeder.viewer.dto.toDto
import li.doerf.feeder.viewer.entities.ItemState
import li.doerf.feeder.viewer.repositories.ItemStateRepository
import li.doerf.feeder.viewer.util.UserUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@PrefixController
@RequestMapping("/items")
class ItemsController @Autowired constructor(
        private val itemRepository: ItemRepository,
        private val userUtils: UserUtils,
        private val itemStateRepository: ItemStateRepository
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @GetMapping("/byFeed/{feedId}")
    fun getAllByFeed(@PathVariable feedId: Long): ResponseEntity<List<ItemDto>> {
        log.debug("retrieving al entries for feed pkey=$feedId")
        val entries = itemRepository.findTop30ByFeedPkeyOrderByPublishedDesc(feedId)
        val itemReadMap = itemStateRepository.findAllByUserAndFeed_Pkey(userUtils.getCurrentUser(), feedId).map { it.item.id to it.isRead }.toMap()
        log.debug("done")
        return ResponseEntity(entries.map { it.toDto(itemReadMap.getOrDefault(it.id, false)) }, HttpStatus.OK)
    }

    @PostMapping("/{itemId}/read")
    fun markAsRead(@PathVariable itemId: Long): ResponseEntity<ItemDto> {
        val item = itemRepository.findById(itemId).orElseThrow{ HttpException("Unknown itemId $itemId", HttpStatus.BAD_REQUEST)}
        log.debug("marking item as read $item")
        val stateOp = itemStateRepository.findByUserAndFeedAndItem(userUtils.getCurrentUser(), item.feed, item)
        if (stateOp.isEmpty) {
            val newState = ItemState(0, userUtils.getCurrentUser(), item, item.feed, true)
            itemStateRepository.save(newState)
        } else {
            val state = stateOp.get()
            state.isRead = true
            itemStateRepository.save(state)
        }
        return ResponseEntity.ok(item.toDto(true))
    }

    @PostMapping("/{itemId}/unread")
    fun markAsUnread(@PathVariable itemId: Long): ResponseEntity<ItemDto> {
        val item = itemRepository.findById(itemId).orElseThrow{ HttpException("Unknown itemId $itemId", HttpStatus.BAD_REQUEST)}
        log.debug("marking item as unread $item")
        val stateOp = itemStateRepository.findByUserAndFeedAndItem(userUtils.getCurrentUser(), item.feed, item)
        if (stateOp.isPresent) {
            val state = stateOp.get()
            state.isRead = false
            itemStateRepository.save(state)
        }
        return ResponseEntity.ok(item.toDto(false))
    }

}
