package li.doerf.feeder.scraper

import li.doerf.feeder.scraper.mqtt.MqttGateway
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
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
    fun testSendNewFeedsMessage() {
        val step = FeedNotifierStep(mqttGateway)
        step.sendMessage(41, true, false)

        verify(mqttGateway).sendToNewFeeds(contains("41"))
    }

    @Test
    fun testSendUpdatedItemsMessage() {
        val step = FeedNotifierStep(mqttGateway)
        step.sendMessage(3, false, true)

        verify(mqttGateway).sendToUpdatedItems(contains("3"));
    }

    @Test
    fun testSendNewFeedAndUpdatedItemsMessage() {
        val step = FeedNotifierStep(mqttGateway)
        step.sendMessage(3, true, true)

        verify(mqttGateway).sendToNewFeeds(anyString())
        verify(mqttGateway, never()).sendToUpdatedItems(ArgumentMatchers.anyString())
    }

}