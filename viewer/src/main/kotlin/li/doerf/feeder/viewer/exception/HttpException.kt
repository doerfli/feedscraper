package li.doerf.feeder.viewer.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class HttpException(override val message: String, val httpStatus: HttpStatus) : ResponseStatusException(httpStatus, message)