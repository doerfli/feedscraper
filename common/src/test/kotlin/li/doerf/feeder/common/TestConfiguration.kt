package li.doerf.feeder.common

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * dummy application required for tests
 */
@SpringBootConfiguration
@EnableJpaRepositories
@EntityScan
class TestConfiguration {
}