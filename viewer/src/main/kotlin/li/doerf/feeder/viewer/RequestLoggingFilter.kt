package li.doerf.feeder.viewer

import li.doerf.feeder.common.util.getLogger
import org.springframework.stereotype.Component
import org.springframework.web.filter.AbstractRequestLoggingFilter
import java.time.Duration
import java.time.Instant
import javax.servlet.http.HttpServletRequest

@Component
class RequestLoggingFilter : AbstractRequestLoggingFilter() {

    private lateinit var startedAt: Instant

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    override fun beforeRequest(request: HttpServletRequest, message: String) {
        startedAt = Instant.now()
        log.debug("received ${request.method} request for [${request.servletPath}]")
    }

    override fun afterRequest(request: HttpServletRequest, message: String) {
        val finishedAt = Instant.now()
        val duration = Duration.between(startedAt, finishedAt)
        log.debug("${request.method} request for [${request.servletPath}] finished in ${duration.toMillis()}ms")
    }

}