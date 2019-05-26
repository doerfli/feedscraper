package li.doerf.feeder.viewer.controller

import li.doerf.feeder.scraper.util.getLogger
import li.doerf.feeder.viewer.dto.EntryDto
import li.doerf.feeder.viewer.dto.toDto
import li.doerf.feeder.viewer.repositories.EntryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class EntriesController @Autowired constructor(
        private val entryRepository: EntryRepository
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @GetMapping("/entries/{feedId}")
    fun getAllByFeed(@PathVariable feedId: Long): ResponseEntity<List<EntryDto>> {
        log.debug("retrieving al entries for feed pkey=$feedId")
        val entries = entryRepository.findTop30ByFeedPkeyOrderByPublishedDesc(feedId).map { it.toDto() }
        log.debug("done")
        return ResponseEntity(entries, HttpStatus.OK)
    }

}
