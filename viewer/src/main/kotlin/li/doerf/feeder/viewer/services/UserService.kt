package li.doerf.feeder.viewer.services

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.HttpException
import li.doerf.feeder.viewer.dto.UserResponseDto
import li.doerf.feeder.viewer.entities.Role
import li.doerf.feeder.viewer.entities.User
import li.doerf.feeder.viewer.repositories.UserRepository
import li.doerf.feeder.viewer.security.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val jwtTokenProvider: JwtTokenProvider,
        private val authenticationManager: AuthenticationManager
){

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    fun signin(username: String, password: String): UserResponseDto {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
            return UserResponseDto(token = jwtTokenProvider.createToken(username, userRepository.findByUsername(username).orElseThrow {throw IllegalArgumentException("invalid username $username")}.roles))
        } catch (e: AuthenticationException) {
            log.warn("could not login user", e)
            throw HttpException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY)
        }

    }

    fun signup(username: String, password: String): UserResponseDto {
        // always encode password to avoid timing attacks
        val encodedPassword = passwordEncoder.encode(password);
        if (userRepository.findByUsername(username).isEmpty) {
            val user = User(0, username, encodedPassword, mutableListOf(Role.ROLE_CLIENT))
            userRepository.save(user)
            log.info("created user $user")
            return UserResponseDto(token = jwtTokenProvider.createToken(username, listOf(Role.ROLE_CLIENT)))
        } else {
            log.warn("username already in use")
            throw HttpException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY)
        }
    }

//    fun delete(username: String) {
//        val user = userRepository.findByUsername(username).orElseThrow { throw IllegalArgumentException("Invalid username $username") }
//        userRepository.delete(user)
//    }

}