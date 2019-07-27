package li.doerf.feeder.scraper.mqtt

import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway

@MessagingGateway(defaultRequestChannel = "channel_feeds")
interface MqttGateway {
    @Gateway
    fun sendToMqtt(data: String)
}