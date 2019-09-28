package li.doerf.feeder.common

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * dummy application required for tests
 */
@SpringBootApplication
@EntityScan("li.doerf.feeder.common")
@EnableJpaRepositories("li.doerf.feeder.common")
class TestConfiguration {
}