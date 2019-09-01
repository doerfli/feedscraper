package li.doerf.feeder.viewer.services

import li.doerf.feeder.viewer.websocket.messages.NewFeedsMessage
import li.doerf.feeder.viewer.websocket.messages.UpdatedItemsMessage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.messaging.simp.SimpMessagingTemplate


@SpringBootTest
@ExtendWith(MockitoExtension::class)
class FeedServiceImplSpringBootTest {

    @Autowired
    private lateinit var feedService: FeedServiceImpl
    @MockBean
    private lateinit var wsTemplate: SimpMessagingTemplate

    @Test
    fun testNotifyClientsAboutNewFeed() {
        feedService.notifyClientsAboutNewFeed("13")

        Mockito.verify(wsTemplate).convertAndSend("/topic/feeds", NewFeedsMessage("13"))
    }

    @Test
    fun testNotifyClientsUpdatedItems() {
        feedService.notifyClientsUpdatedItems("42")

        Mockito.verify(wsTemplate).convertAndSend("/topic/updated_items", UpdatedItemsMessage("42"))
    }
}