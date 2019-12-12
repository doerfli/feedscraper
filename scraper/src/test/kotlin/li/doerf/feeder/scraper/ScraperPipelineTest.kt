package li.doerf.feeder.scraper

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import li.doerf.feeder.common.repositories.FeedRepository
import li.doerf.feeder.scraper.MockitoTestHelper.Companion.any
import li.doerf.feeder.scraper.dto.FeedDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@Import(FeedParserStep::class, FeedDownloaderStep::class, FeedPersisterStep::class, FeedNotifierStep::class)
class ScraperPipelineTest {

    @MockBean
    private lateinit var feedDownloaderStep: FeedDownloaderStep
    @MockBean
    private lateinit var feedParserStep: FeedParserStep
    @MockBean
    private lateinit var feedPersisterStep: FeedPersisterStep
    @MockBean
    private lateinit var feedNotifierStep: FeedNotifierStep
    // SUT
    private lateinit var pipeline: ScraperPipeline

    @BeforeEach
    fun beforeEach() {
        pipeline = ScraperPipeline(
                mock(FeedRepository::class.java),
                feedDownloaderStep,
                feedParserStep,
                feedPersisterStep,
                feedNotifierStep
        )
        pipeline = spy(pipeline)
    }

    @Test
    fun testOneUri() {
        runBlocking {
            // given
            val uri = "http://localhost/"
            val content = "content"
            val feedDto = mock(FeedDto::class.java)
            val pkey = 42L
            val firstDownload = true
            val itemsUpdated = false

            `when`(pipeline.generateFeedUrls()).thenReturn(flow { emit(uri) })

            `when`(feedDownloaderStep.download(uri)).thenReturn(DownloadSuccess(uri, content))
            `when`(feedParserStep.parse(uri, content)).thenReturn(ParserSuccess(uri, feedDto))
            `when`(feedPersisterStep.persist(uri, feedDto)).thenReturn(PersisterSuccess(firstDownload, itemsUpdated, pkey))

            // when
            pipeline.startPipeline()

            // then
            verify(feedNotifierStep).sendMessage(pkey, firstDownload, itemsUpdated)
        }

    }
    @Test
    fun testDownloadFailed() {
        runBlocking {
            // given
            val uri = "http://localhost/"

            `when`(pipeline.generateFeedUrls()).thenReturn(flow { emit(uri) })

            `when`(feedDownloaderStep.download(uri)).thenReturn(DownloadError("failure"))

            // when
            pipeline.startPipeline()

            // then
            verify(feedParserStep, never()).parse(any(), any())
            verify(feedPersisterStep, never()).persist(any(), any())
            verify(feedNotifierStep, never()).sendMessage(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyBoolean())
        }
    }
}
