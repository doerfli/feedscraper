package li.doerf.feeder.viewer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ViewerApplication

fun main(args: Array<String>) {
    runApplication<ViewerApplication>(*args)
}
