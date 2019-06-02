package li.doerf.feeder.viewer

import org.springframework.http.HttpStatus

class HttpException(override val message: String, val httpStatus: HttpStatus) : Exception(message)