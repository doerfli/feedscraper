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

    fun sendNotificationEvent() {
        mqttGateway.sendToMqtt("new_feed")
        log.debug("sent feed notification  mqtt")
    }

}