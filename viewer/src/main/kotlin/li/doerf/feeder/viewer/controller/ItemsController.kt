package li.doerf.feeder.viewer.controller

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.dto.ItemDto
import li.doerf.feeder.viewer.dto.toDto
import li.doerf.feeder.viewer.repositories.ItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ItemsController @Autowired constructor(
        private val itemRepository: ItemRepository
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @GetMapping("/items/{feedId}")
    fun getAllByFeed(@PathVariable feedId: Long): ResponseEntity<List<ItemDto>> {
        log.debug("retrieving al entries for feed pkey=$feedId")
        val entries = itemRepository.findTop30ByFeedPkeyOrderByPublishedDesc(feedId).map { it.toDto() }
        log.debug("done")
        return ResponseEntity(entries, HttpStatus.OK)
    }

}
