package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.common.repositories.ItemRepository
import li.doerf.feeder.common.repositories.ItemStateRepository
import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.dto.ItemDto
import li.doerf.feeder.viewer.dto.toDto
import li.doerf.feeder.viewer.util.UserUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

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

    @GetMapping("/{feedId}")
    fun getAllByFeed(@PathVariable feedId: Long, principal: Principal): ResponseEntity<List<ItemDto>> {
        log.debug("retrieving al entries for feed pkey=$feedId")
        val entries = itemRepository.findTop30ByFeedPkeyOrderByPublishedDesc(feedId)
        val itemReadMap = itemStateRepository.findAllByUserAndFeed_Pkey(userUtils.getCurrentUser(), feedId).map { it.item.id to it.isRead }.toMap()
        log.debug("done")
        return ResponseEntity(entries.map { it.toDto() }.map{ it.read = itemReadMap.getOrDefault(it.id, false); it }, HttpStatus.OK)
    }

}
