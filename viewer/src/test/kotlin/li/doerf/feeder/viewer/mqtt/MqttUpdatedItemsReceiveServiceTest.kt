package li.doerf.feeder.viewer.mqtt

import li.doerf.feeder.viewer.services.FeedService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@Import(FeedService::class, MqttUpdatedItemsReceiveService::class)
class MqttUpdatedItemsReceiveServiceTest {

    @MockBean
    private lateinit var feedService: FeedService
    @Autowired
    private lateinit var mqttUpdatedItemsReceiveService: MqttUpdatedItemsReceiveService

    @Test
    fun testReceive() {
        mqttUpdatedItemsReceiveService.receive("42")

        Mockito.verify(feedService).notifyClientsUpdatedItems("42")
    }
}