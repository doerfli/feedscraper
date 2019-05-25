package li.doerf.feeder.scraper

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import li.doerf.feeder.scraper.util.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FeedDownloaderStep @Autowired constructor(
        private val fuel: Fuel
){

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    suspend fun download(uri: String): String {
        log.debug("downloading $uri")
        val (request, response, result) = fuel.get(uri).awaitStringResponseResult()
        log.debug("downloaded ${response.contentLength} bytes - status ${response.statusCode}")
        if (response.statusCode != 200) {
            throw IllegalStateException("received statuscode ${response.statusCode} - download not successful")
        }
        val content = result.get()
        if(log.isTraceEnabled) log.trace(content)
        return content
    }
}