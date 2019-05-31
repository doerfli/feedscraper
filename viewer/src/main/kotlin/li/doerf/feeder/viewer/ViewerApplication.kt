package li.doerf.feeder.viewer

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.controller.PrefixController
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@SpringBootApplication
@Configuration
//@ComponentScan(basePackages = ["li.doerf.feeder"])
@EntityScan("li.doerf.feeder.common.entities")
@EnableJpaRepositories("li.doerf.feeder.common.repositories")
class ViewerApplication {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
        private const val PREFIX = "/api"
    }

    @Bean
    fun webMvcConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:8070")
            }

            override fun configurePathMatch(configurer: PathMatchConfigurer) {
                configurer.addPathPrefix(PREFIX) { c -> c.isAnnotationPresent(PrefixController::class.java) }
            }
        }
    }

}

fun main(args: Array<String>) {
    runApplication<ViewerApplication>(*args)
}
