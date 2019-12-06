package li.doerf.feeder.scraper

import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.github.kittinunf.fuel.httpGet
import li.doerf.feeder.common.util.getLogger
import org.springframework.stereotype.Service

@Service
class FeedDownloaderStep {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    suspend fun download(uri: String): Pair<String, String> {
        log.debug("downloading $uri")
        val (request, response, result) =
                uri.httpGet().awaitStringResponseResult()
        log.debug("downloaded ${response.contentLength} bytes - status ${response.statusCode}")
        if ( ! response.isSuccessful) {
            throw IllegalStateException("received statuscode ${response.statusCode} - download not successful")
        }
        val content = result.get()
        if(log.isTraceEnabled) log.trace(content)
        return Pair(uri, content)
    }
}