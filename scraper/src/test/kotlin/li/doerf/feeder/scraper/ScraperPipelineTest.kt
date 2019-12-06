package li.doerf.feeder.scraper

import li.doerf.feeder.common.repositories.FeedRepository
import org.junit.jupiter.api.extension.ExtendWith
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


}