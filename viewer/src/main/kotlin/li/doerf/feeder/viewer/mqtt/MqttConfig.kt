package li.doerf.feeder.viewer.mqtt

import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.endpoint.MessageProducerSupport
import org.springframework.integration.handler.LoggingHandler
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.core.MqttPahoClientFactory
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter

@Configuration
class MqttConfig {


    @Value("\${mqtt.serverUri:tcp://localhost:1883}")
    private val serverUri: String = "tcp://localhost:1883"
    @Value("\${mqtt.username:guest}")
    private val username: String = "guest"
    @Value("\${mqtt.password:guest}")
    private val password: String = "guest"

    @Autowired
    private lateinit var mqttNewFeedReceiveService: MqttNewFeedReceiveService

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
    fun mqttInFlow(): IntegrationFlow {
        return IntegrationFlows.from(mqttInbound())
                .transform<Any, String> { p -> "$p, received from MQTT" }
                .handle(mqttNewFeedReceiveService)
                .handle(logger())
                .get()
    }

    private fun logger(): LoggingHandler {
        val loggingHandler = LoggingHandler("INFO")
        loggingHandler.setLoggerName("MqttReceiver")
        return loggingHandler
    }

    @Bean
    fun mqttInbound(): MessageProducerSupport {
        val adapter = MqttPahoMessageDrivenChannelAdapter("viewer",
                mqttClientFactory(), "new_feed")
        adapter.setCompletionTimeout(5000)
        adapter.setConverter(DefaultPahoMessageConverter())
        adapter.setQos(1)
        return adapter
    }

}

