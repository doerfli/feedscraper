package li.doerf.feeder.scraper

import com.github.kittinunf.fuel.Fuel
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.concurrent.Executors

@SpringBootApplication
class ScraperApplication {

    @Bean
    fun fuel(): Fuel {
        return Fuel
    }

}

fun main(args: Array<String>) {
    val context = runApplication<ScraperApplication>(*args)

    // TODO kotlin stype
    Executors.newSingleThreadExecutor().execute {
        context.getBean(ScraperPipeline::class.java).execute()
    }
}
