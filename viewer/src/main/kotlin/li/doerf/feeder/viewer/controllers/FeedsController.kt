package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.dto.FeedAddRequestDto
import li.doerf.feeder.viewer.dto.FeedDto
import li.doerf.feeder.viewer.dto.toDto
import li.doerf.feeder.viewer.services.FeedService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@PrefixController
@RequestMapping("/feeds")
class FeedsController @Autowired constructor(
        private val feedService: FeedService
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @GetMapping
    fun index(): ResponseEntity<List<FeedDto>> {
        log.debug("retrieving all active feeds")
        val feeds = feedService.getAllActiveFeeds()
        return ResponseEntity.ok(feeds.map { it.toDto() })
    }

    @PostMapping
    fun add(@RequestBody feedAddDto: FeedAddRequestDto): HttpStatus {
        log.debug("request to add new feed url: $feedAddDto")
        feedService.add(feedAddDto.url)
//        feedService.waitForFeedScrapeAndNotifyClient(feedAddDto.url)
        return HttpStatus.OK
    }

}