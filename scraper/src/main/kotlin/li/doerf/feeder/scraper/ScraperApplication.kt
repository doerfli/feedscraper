package li.doerf.feeder.scraper

import com.github.kittinunf.fuel.Fuel
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.util.concurrent.Executors


@SpringBootApplication
//@ComponentScan(basePackages = ["li.doerf.feeder"])
@EntityScan("li.doerf.feeder.common.entities")
@EnableJpaRepositories("li.doerf.feeder.common.repositories")
class ScraperApplication {

    @Bean
    fun fuel(): Fuel {
        return Fuel
    }

}

fun main(args: Array<String>) {
    val context = runApplication<ScraperApplication>(*args)

    // TODO kotlin style
    Executors.newSingleThreadExecutor().execute {
        context.getBean(ScraperPipeline::class.java).execute()
    }
    context
}
