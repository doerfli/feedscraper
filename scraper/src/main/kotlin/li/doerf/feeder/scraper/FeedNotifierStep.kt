package li.doerf.feeder.scraper

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.scraper.mqtt.MqttGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FeedNotifierStep @Autowired constructor(
        @Suppress("SpringJavaInjectionPointsAutowiringInspection") private val mqttGateway: MqttGateway
){
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    fun sendMessage(feedPkey: Long, newFeedDownloaded: Boolean, itemsUpdated: Boolean) {
        if (newFeedDownloaded) {
            sendNewFeedMessage(feedPkey)
        } else if (itemsUpdated) {
            sendItemsUpdatedMessage(feedPkey)
        }
    }

    private fun sendNewFeedMessage(feedPkey: Long) {
        mqttGateway.sendToNewFeeds("$feedPkey")
        log.debug("sent new feed message to mqtt")
    }

    private fun sendItemsUpdatedMessage(feedId: Long) {
        mqttGateway.sendToUpdatedItems("$feedId")
        log.debug("sent updated items message to mqtt")
    }

}