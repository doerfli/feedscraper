package li.doerf.feeder.scraper

import com.github.kittinunf.fuel.Fuel
import li.doerf.feeder.scraper.ScraperApplication.MyGateway
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.integration.annotation.MessagingGateway
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.endpoint.MessageProducerSupport
import org.springframework.integration.handler.LoggingHandler
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.core.MqttPahoClientFactory
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter
import org.springframework.messaging.MessageHandler
import java.util.concurrent.Executors


@SpringBootApplication
//@ComponentScan(basePackages = ["li.doerf.feeder"])
@EntityScan("li.doerf.feeder.common.entities")
@EnableJpaRepositories("li.doerf.feeder.common.repositories")
class ScraperApplication {

    @Bean
    fun fuel(): Fuel {
        return Fuel
    }


    @Bean
    fun mqttClientFactory(): MqttPahoClientFactory {
        val factory = DefaultMqttPahoClientFactory()
        val options = MqttConnectOptions()
        options.serverURIs = arrayOf("tcp://localhost:1883")
        options.userName = "guest"
        options.password = "guest".toCharArray()
        factory.connectionOptions = options
        return factory
    }

    // publisher

    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    interface MyGateway {

        fun sendToMqtt(data: String)

    }



    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    fun mqttOutbound(): MessageHandler {
        val messageHandler = MqttPahoMessageHandler("siSamplePublisher", mqttClientFactory())
        messageHandler.setAsync(true)
        messageHandler.setDefaultTopic("siSampleTopic")
        return messageHandler
    }

    // consumer

    @Bean
    fun mqttInFlow(): IntegrationFlow {
        return IntegrationFlows.from(mqttInbound())
                .transform<Any, String> { p -> "$p, received from MQTT" }
                .handle(logger())
                .get()
    }

    private fun logger(): LoggingHandler {
        val loggingHandler = LoggingHandler("INFO")
        loggingHandler.setLoggerName("siSample")
        return loggingHandler
    }

    @Bean
    fun mqttInbound(): MessageProducerSupport {
        val adapter = MqttPahoMessageDrivenChannelAdapter("siSampleConsumer",
                mqttClientFactory(), "siSampleTopic")
        adapter.setCompletionTimeout(5000)
        adapter.setConverter(DefaultPahoMessageConverter())
        adapter.setQos(1)
        return adapter
    }

}

fun main(args: Array<String>) {
    val context = runApplication<ScraperApplication>(*args)

    // TODO kotlin style
    Executors.newSingleThreadExecutor().execute {
//        context.getBean(ScraperPipeline::class.java).execute()
        val gateway = context.getBean(MyGateway::class.java)
        gateway.sendToMqtt("foo")
    }
}
