package li.doerf.feeder.scraper

import li.doerf.feeder.scraper.mqtt.MqttGateway
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@Import(FeedNotifierStep::class, MqttGateway::class)
class FeedNotifierStepTest {

    @MockBean
    private lateinit var mqttGateway: MqttGateway

    @Test
    fun testSendNotificationEvent() {
        val step = FeedNotifierStep(mqttGateway)
        step.sendNewFeedDownloadedNotification()

        Mockito.verify(mqttGateway).sendToMqtt(Mockito.anyString())
    }

}