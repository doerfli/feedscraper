package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.dto.UserRequestDto
import li.doerf.feeder.viewer.dto.UserResponseDto
import li.doerf.feeder.viewer.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@PrefixController
@RequestMapping("/users")
class UserController @Autowired constructor(
        private val userService: UserService
){

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @PostMapping("/signin")
    fun login(@RequestBody userRequest: UserRequestDto): ResponseEntity<UserResponseDto> {
        log.debug("login user ${userRequest.username}")
        val dto = userService.signin(userRequest.username, userRequest.password)
        return ResponseEntity.ok(dto)
    }

    @PostMapping("/signup")
    fun signup(@RequestBody userRequest: UserRequestDto): ResponseEntity<UserResponseDto> {
        log.debug("signup user ${userRequest.username}")
        val dto = userService.signup(userRequest.username, userRequest.password)
        return ResponseEntity.ok(dto)
    }

}