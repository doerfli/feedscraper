package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.dto.FeedDto
import li.doerf.feeder.viewer.dto.toDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@PrefixController
class FeedsController @Autowired constructor(
        private val feedRepository: FeedRepository
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @GetMapping("/feeds")
    fun index(): ResponseEntity<List<FeedDto>> {
        log.debug("retrieving all active feeds")
        val feeds = feedRepository.findAllByTitleNotNull(Sort.by(Sort.Order.asc("title").ignoreCase())).map { it.toDto() }
        log.debug("done")
        return ResponseEntity(feeds, HttpStatus.OK)
    }

}