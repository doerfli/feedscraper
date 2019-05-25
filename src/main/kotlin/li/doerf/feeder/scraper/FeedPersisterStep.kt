package li.doerf.feeder.scraper

import li.doerf.feeder.scraper.dto.EntryDto
import li.doerf.feeder.scraper.dto.FeedDto
import li.doerf.feeder.scraper.entities.Entry
import li.doerf.feeder.scraper.entities.Feed
import li.doerf.feeder.scraper.repositories.EntryRepository
import li.doerf.feeder.scraper.repositories.FeedRepository
import li.doerf.feeder.scraper.util.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class FeedPersisterStep @Autowired constructor(
        val feedRepository: FeedRepository,
        val entryRepository: EntryRepository
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @Transactional
    fun persist(uri: String, feedDto: FeedDto) {
        log.debug("starting to persist feed for $uri")
        val feed = feedRepository.findFeedByUrl(uri).orElseThrow()
        feed.lastDownloaded = Instant.now()

        if (feed.updated != feedDto.updated) {
            log.debug("updating feed")
            updateFeed(feedDto, feed)
            updateEntries(feedDto.entries, feed)
            log.info("feed and entries updated")
        } else {
            log.debug("feed did not change - nothing to update")
        }

        feedRepository.save(feed)
        log.trace("feed saved $feed")
    }

    private fun updateFeed(feedDto: FeedDto, feed: Feed) {
        // update dates
        feed.updated = feedDto.updated

        if(feed.title != null && feed.title != "") { // base values already set
            log.debug("feed did not change")
            return
        }

        feed.id = feedDto.id
        feed.title = feedDto.title
        feed.subtitle = feedDto.subtitle
        feed.linkAlternate = feedDto.linkAlternate
        feed.linkSelf = feedDto.linkSelf
        log.debug("feed updated")
    }

    private fun updateEntries(downloadedEntries: MutableList<EntryDto>, feed: Feed) {
        val entries = entryRepository.findAllByFeed(feed)
        val entriesMap = entries.map{ it.id to it}.toMap()

        downloadedEntries.forEach{ dEntry ->
            val entry = entriesMap[dEntry.id]
            if (entry != null) {
                if (dEntry.updated != entry.updated) {
                    updateEntry(entry, dEntry)
                }
            } else {
                createEntry(feed, dEntry)
            }
        }
    }

    private fun createEntry(feed: Feed, dEntry: EntryDto) {
        val newEntry = Entry(
                0,
                feed,
                dEntry.id,
                dEntry.title,
                dEntry.link,
                dEntry.summary,
                dEntry.content,
                dEntry.published,
                dEntry.updated)
        entryRepository.save(newEntry)
        log.debug("saved new entry ${dEntry.id}")
    }

    private fun updateEntry(entry: Entry, dEntry: EntryDto) {
        // entry has changed
        entry.title = dEntry.title
        entry.summary = dEntry.summary
        entry.content = dEntry.content
        entry.link = dEntry.link
        entry.updated = dEntry.updated
        entryRepository.save(entry)
        log.debug("updated entry ${entry.id}")
    }

}