package li.doerf.feeder.viewer.mqtt

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.services.FeedService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MqttNewFeedReceiveService @Autowired constructor(
        private val feedService: FeedService
){

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    fun greeting(payload: String) {
        log.debug("received msg: $payload")
        feedService.notifyClientsAboutNewFeed(payload)
    }
}