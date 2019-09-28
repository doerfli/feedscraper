package li.doerf.feeder.scraper

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import li.doerf.feeder.common.entities.Feed
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.scraper.dto.FeedDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@Import(ScraperPipeline::class, FeedParserStep::class, FeedDownloaderStep::class, FeedPersisterStep::class, FeedNotifierStep::class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ScraperPipelineTest {

    @Autowired
    private lateinit var feedRepository: FeedRepository
    @MockBean
    private lateinit var feedDownloaderStep: FeedDownloaderStep
    @MockBean
    private lateinit var feedParserStep: FeedParserStep
    @MockBean
    private lateinit var feedPersisterStep: FeedPersisterStep
    @MockBean
    private lateinit var feedNotifierStep: FeedNotifierStep
    @Autowired
    private lateinit var pipeline: ScraperPipeline

    @Test
    fun testParseFeed() {
        val feedDtoMock = mock(FeedDto::class.java)
        val channelMock = mock(Channel::class.java) as Channel<Pair<String, FeedDto>>
        doReturn(feedDtoMock).`when`(feedParserStep).parse(MockitoTestHelper.any(), MockitoTestHelper.any())

        runBlocking {
            pipeline.parseFeed("http://blubb.com/feed", "feedContents", channelMock)

            verify(channelMock).send(Pair(MockitoTestHelper.any(), feedDtoMock))
        }
    }

    @Test
    fun testParseFeedException() {
        val feed = Feed(0,"http://blubb.com/feed", retry = 1)
        feedRepository.save(feed)

        val feedDtoMock = mock(FeedDto::class.java)
        val channelMock = mock(Channel::class.java) as Channel<Pair<String, FeedDto>>
        doThrow(IllegalArgumentException("happened")).`when`(feedParserStep).parse(MockitoTestHelper.any(), MockitoTestHelper.any())

        runBlocking {
            pipeline.parseFeed(feed.url, "feedContents", channelMock)

            verify(channelMock, never()).send(Pair(MockitoTestHelper.any(), feedDtoMock))

            val feedAfterTestOpt = feedRepository.findFeedByUrl(feed.url)
            assertThat(feedAfterTestOpt.isPresent).isTrue()
            val feedAfterTest = feedAfterTestOpt.get()
            assertThat(feedAfterTest.retry).isEqualTo(2)
        }
    }

    @Test
    fun testPersistFeed() {
        val feedDtoMock = mock(FeedDto::class.java)
        val resultMock = mock(FeedPersisterResult::class.java)
        val channelMock = mock(Channel::class.java) as Channel<FeedPersisterResult>
        doReturn(resultMock).`when`(feedPersisterStep).persist(MockitoTestHelper.any(), MockitoTestHelper.any())

        runBlocking {
            pipeline.persistFeed("http://blubb.com/feed", feedDtoMock, channelMock)

            verify(channelMock).send(resultMock)
        }
    }

    @Test
    fun testPersistFeedException() {
        val feed = Feed(0,"http://blubb.com/feed", retry = 2)
        feedRepository.save(feed)

        val feedDtoMock = mock(FeedDto::class.java)
        val resultMock = mock(FeedPersisterResult::class.java)
        val channelMock = mock(Channel::class.java) as Channel<FeedPersisterResult>
        doThrow(IllegalArgumentException("happened")).`when`(feedPersisterStep).persist(MockitoTestHelper.any(), MockitoTestHelper.any())

        runBlocking {
            pipeline.persistFeed("http://blubb.com/feed", feedDtoMock, channelMock)

            verify(channelMock, never()).send(MockitoTestHelper.any())

            val feedAfterTestOpt = feedRepository.findFeedByUrl(feed.url)
            assertThat(feedAfterTestOpt.isPresent).isTrue()
            val feedAfterTest = feedAfterTestOpt.get()
            assertThat(feedAfterTest.retry).isEqualTo(3)
        }
    }
}