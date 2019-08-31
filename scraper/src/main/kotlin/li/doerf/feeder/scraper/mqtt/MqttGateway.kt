package li.doerf.feeder.scraper.mqtt

import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway

@MessagingGateway(defaultRequestChannel = "new_feed")
interface MqttGateway {
    @Gateway(requestChannel = "new_feed")
    fun sendToNewFeeds(data: String)
    @Gateway(requestChannel = "updated_items")
    fun sendToUpdatedItems(data: String)
}