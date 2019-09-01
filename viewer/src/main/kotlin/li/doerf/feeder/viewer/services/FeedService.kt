package li.doerf.feeder.viewer.services

import li.doerf.feeder.common.entities.Feed

interface FeedService {
    fun getAllActiveFeeds(): Collection<Feed>
    fun get(pkey: Long): Feed
    fun add(url: String): Feed
    fun notifyClientsAboutNewFeed(feedPkey: String)
    fun notifyClientsUpdatedItems(feedPkey: String)
}