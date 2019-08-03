package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.dto.FeedAddRequestDto
import li.doerf.feeder.viewer.dto.FeedDto
import li.doerf.feeder.viewer.dto.toDto
import li.doerf.feeder.viewer.services.FeedService
import li.doerf.feeder.viewer.services.ItemService
import li.doerf.feeder.viewer.util.UserUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@PrefixController
@RequestMapping("/feeds")
class FeedsController @Autowired constructor(
        private val feedService: FeedService,
        private val itemService: ItemService,
        private val userUtils: UserUtils
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @GetMapping
    fun index(): ResponseEntity<List<FeedDto>> {
        log.debug("retrieving all active feeds")
        val feeds = feedService.getAllActiveFeeds()
        val unreadItemsMap = itemService.getUnreadItemsCount(feeds.map { it.pkey }, userUtils.getCurrentUser())
        return ResponseEntity.ok(feeds.map { it.toDto(unreadItemsMap.getOrDefault(it.pkey, 0)) })
    }

    @PostMapping
    fun add(@RequestBody feedAddDto: FeedAddRequestDto): HttpStatus {
        log.debug("request to add new feed url: $feedAddDto")
        feedService.add(feedAddDto.url)
//        feedService.waitForFeedScrapeAndNotifyClient(feedAddDto.url)
        return HttpStatus.OK
    }

}