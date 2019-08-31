package li.doerf.feeder.scraper.mqtt

import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.core.MqttPahoClientFactory
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler
import org.springframework.messaging.MessageHandler

@Configuration
class MqttConfig {


    @Value("\${mqtt.serverUri:tcp://localhost:1883}")
    private val serverUri: String = "tcp://localhost:1883"
    @Value("\${mqtt.username:guest}")
    private val username: String = "guest"
    @Value("\${mqtt.password:guest}")
    private val password: String = "guest"

    @Bean
    fun mqttClientFactory(): MqttPahoClientFactory {
        val factory = DefaultMqttPahoClientFactory()
        val options = MqttConnectOptions()
        options.serverURIs = arrayOf(serverUri)
        options.userName = username
        options.password = password.toCharArray()
        factory.connectionOptions = options
        return factory
    }

    @Bean
    @ServiceActivator(inputChannel = "new_feed")
    fun mqttChannelNewFeeds(): MessageHandler {
        val messageHandler = MqttPahoMessageHandler("scraper_nf", mqttClientFactory())
        messageHandler.setAsync(true)
        messageHandler.setDefaultTopic("new_feed")
        return messageHandler
    }

    @Bean
    @ServiceActivator(inputChannel = "updated_items")
    fun mqttChannelUpdateItems(): MessageHandler {
        val messageHandler = MqttPahoMessageHandler("scraper_ui", mqttClientFactory())
        messageHandler.setAsync(true)
        messageHandler.setDefaultTopic("updated_items")
        return messageHandler
    }

}