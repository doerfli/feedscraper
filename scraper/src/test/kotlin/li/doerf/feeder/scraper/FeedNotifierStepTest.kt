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
        val result = FeedPersisterResult(newFeedDownloaded = true, itemsUpdated = false, feedPkey = 0)
        step.sendMessage(result)

        verify(mqttGateway).sendToNewFeeds(anyString())
    }

    @Test
    fun testSendUpdatedItemsMessage() {
        val step = FeedNotifierStep(mqttGateway)
        val result = FeedPersisterResult(newFeedDownloaded = false, itemsUpdated = true, feedPkey = 3)
        step.sendMessage(result)

        verify(mqttGateway).sendToUpdatedItems(contains("3"));
    }

    @Test
    fun testSendNewFeedAndUpdatedItemsMessage() {
        val step = FeedNotifierStep(mqttGateway)
        val result = FeedPersisterResult(newFeedDownloaded = true, itemsUpdated = true, feedPkey = 3)
        step.sendMessage(result)

        verify(mqttGateway).sendToNewFeeds(anyString())
        verify(mqttGateway, never()).sendToUpdatedItems(ArgumentMatchers.anyString())
    }

}