package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.dto.ItemDto
import li.doerf.feeder.viewer.dto.toDto
import li.doerf.feeder.viewer.services.ItemService
import li.doerf.feeder.viewer.util.UserUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@PrefixController
@RequestMapping("/items")
class ItemsController @Autowired constructor(
        private val userUtils: UserUtils,
        private val itemService: ItemService
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @GetMapping("/byFeed/{feedId}")
    fun getAllByFeed(
            @PathVariable feedId: Long,
            @RequestParam(required = false) fromPkey: Long?,
            @RequestParam(required = false, defaultValue = "30") size: Int
    ): ResponseEntity<List<ItemDto>> {
        log.debug("retrieving al entries for feed pkey=$feedId")
        val itemDtos = itemService.getItemsByFeed(feedId, userUtils.getCurrentUser(), fromPkey, size)
        return ResponseEntity(itemDtos, HttpStatus.OK)
    }


    @PostMapping("/{itemId}/read")
    fun markAsRead(@PathVariable itemId: Long): ResponseEntity<ItemDto> {
        val item = itemService.markAsRead(itemId, userUtils.getCurrentUser())
        return ResponseEntity.ok(item.toDto(true))
    }



    @PostMapping("/{itemId}/unread")
    fun markAsUnread(@PathVariable itemId: Long): ResponseEntity<ItemDto> {
        val item = itemService.markAsUnread(itemId, userUtils.getCurrentUser())
        return ResponseEntity.ok(item.toDto(false))
    }



}
