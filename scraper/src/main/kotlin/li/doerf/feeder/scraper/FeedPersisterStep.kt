package li.doerf.feeder.scraper

import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.entities.Item
import li.doerf.feeder.scraper.dto.FeedDto
import li.doerf.feeder.scraper.dto.ItemDto
import li.doerf.feeder.scraper.repositories.FeedRepository
import li.doerf.feeder.scraper.repositories.ItemRepository
import li.doerf.feeder.common.util.getLogger
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
    fun persist(uri: String, feedDto: FeedDto) {
        log.debug("starting to persist feed for $uri")
        val feed = feedRepository.findFeedByUrl(uri).orElseThrow()
        feed.lastDownloaded = Instant.now()

        if (feed.updated != feedDto.updated) {
            log.debug("updating feed")
            updateFeed(feedDto, feed)
            updateItems(feedDto.items, feed)
            log.info("feed and items updated")
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

    private fun updateItems(downloadedItems: MutableList<ItemDto>, feed: Feed) {
        val entries = itemRepository.findAllByFeed(feed)
        val entriesMap = entries.map{ it.id to it}.toMap()

        downloadedItems.forEach{ dItem ->
            val item = entriesMap[dItem.id]
            if (item != null) {
                if (dItem.updated != item.updated) {
                    updateEntry(item, dItem)
                }
            } else {
                createItem(feed, dItem)
            }
        }
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