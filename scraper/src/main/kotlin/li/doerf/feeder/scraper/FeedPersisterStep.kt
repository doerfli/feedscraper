package li.doerf.feeder.scraper

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.Item
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.common.repositories.ItemRepository
import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.scraper.dto.FeedDto
import li.doerf.feeder.scraper.dto.ItemDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class FeedPersisterStep @Autowired constructor(
        val feedRepository: FeedRepository,
        val itemRepository: ItemRepository
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @Transactional
    fun persist(uri: String, feedDto: FeedDto): FeedPersisterResult {
        log.debug("starting to persist feed for $uri")
        val feed = feedRepository.findFeedByUrl(uri).orElseThrow()
        feed.lastDownloaded = Instant.now()
        var firstDownload = false
        var itemsUpdated = false

        if (feed.updated != feedDto.updated) {
            log.debug("updating feed")
            firstDownload = updateFeed(feedDto, feed)
            itemsUpdated = updateItems(feedDto.items, feed)
            log.info("feed and items updated")
        } else {
            log.debug("feed did not change - nothing to update")
        }

        feedRepository.save(feed)
        log.trace("feed saved $feed")

        return FeedPersisterResult(firstDownload, itemsUpdated, feed.pkey)
    }

    private fun updateFeed(feedDto: FeedDto, feed: Feed): Boolean {
        // update dates
        if (feedDto.updated > Instant.MIN) {
            feed.updated = feedDto.updated
        } else {
            feed.updated = findMostRecentUpdateTime(feedDto)
        }

        if(feed.title != null && feed.title != "") { // base values already set
            log.debug("feed did not change")
            return false
        }

        feed.id = feedDto.id
        feed.title = feedDto.title
        feed.subtitle = feedDto.subtitle
        feed.linkAlternate = feedDto.linkAlternate
        feed.linkSelf = feedDto.linkSelf
        log.debug("feed updated")
        return true
    }

    private fun findMostRecentUpdateTime(feedDto: FeedDto): Instant {
        val recentItem = feedDto.items.maxBy { it.updated } ?: throw IllegalArgumentException("feed contained no items")
        return recentItem.updated
    }

    private fun updateItems(downloadedItems: MutableList<ItemDto>, feed: Feed): Boolean {
        val entries = itemRepository.findAllByFeed(feed)
        val entriesMap = entries.map{ it.id to it}.toMap()
        var feedUpdated = false

        downloadedItems.forEach{ dItem ->
            val item = entriesMap[dItem.id]
            if (item != null) {
                if (dItem.updated != item.updated) {
                    updateEntry(item, dItem)
                    feedUpdated = true
                }
            } else {
                createItem(feed, dItem)
                feedUpdated = true
            }
        }

        return feedUpdated
    }

    private fun createItem(feed: Feed, dItem: ItemDto) {
        val item = Item(
                0,
                feed,
                dItem.id,
                dItem.title,
                dItem.link,
                dItem.summary,
                dItem.content,
                dItem.published,
                dItem.updated)
        itemRepository.save(item)
        log.debug("saved new item ${dItem.id}")
    }

    private fun updateEntry(item: Item, dItem: ItemDto) {
        // item has changed
        item.title = dItem.title
        item.summary = dItem.summary
        item.content = dItem.content
        item.link = dItem.link
        item.updated = dItem.updated
        itemRepository.save(item)
        log.debug("updated item ${item.id}")
    }

}

public data class FeedPersisterResult(
        val newFeedDownloaded: Boolean,
        val itemsUpdated: Boolean,
        val feedPkey: Long
)