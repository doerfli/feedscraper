package li.doerf.feeder.common.repositories

import li.doerf.feeder.common.entities.Feed
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FeedRepositoryTest {

    @Autowired
    private lateinit var feedRepository: FeedRepository

    @Test
    fun testFindNotRetryExceeded() {
        val feed1 = Feed(0, "http://host1/feed", retry = 0)
        feedRepository.save(feed1)

        val feed2 = Feed(0, "http://host2/feed", retry = 3)
        feedRepository.save(feed2)

        val feed3 = Feed(0, "http://host3/feed", retry = 4)
        feedRepository.save(feed3)

        val feedList = feedRepository.findNotRetryExceeded()
        assertThat(feedList.size).isEqualTo(2)
    }

}