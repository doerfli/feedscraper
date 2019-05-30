package li.doerf.feeder.viewer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@SpringBootApplication
@Configuration
//@ComponentScan(basePackages = ["li.doerf.feeder"])
@EntityScan("li.doerf.feeder.common.entities")
@EnableJpaRepositories("li.doerf.feeder.common.repositories")
class ViewerApplication {

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:8070")
            }
        }
    }

}

fun main(args: Array<String>) {
    runApplication<ViewerApplication>(*args)
}
