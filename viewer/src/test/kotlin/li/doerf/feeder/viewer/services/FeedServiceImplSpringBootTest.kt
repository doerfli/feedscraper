package li.doerf.feeder.viewer.services

import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.viewer.websocket.messages.NewFeedsMessage
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
    private lateinit var feedRepository: FeedRepository
    @Autowired
    private lateinit var feedService: FeedServiceImpl
    @MockBean
    private lateinit var wsTemplate: SimpMessagingTemplate

    @Test
    fun testWaitForFeedScrapeAndNotifyClient() {
        val url = "http://www.heise.de/feeds"
        val feed = feedService.add(url)

        feedService.waitForFeedScrapeAndNotifyClient(feed.url)

        Thread.sleep(100)

        feed.title = "sometitle"
        feedRepository.save(feed)

        Thread.sleep(2000)

        Mockito.verify(wsTemplate).convertAndSend("/topic/feeds", NewFeedsMessage("new"))
    }


}