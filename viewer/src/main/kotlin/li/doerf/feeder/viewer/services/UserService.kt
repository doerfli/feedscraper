package li.doerf.feeder.viewer.services

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.config.JwtTokenProvider
import li.doerf.feeder.viewer.dto.UserResponseDto
import li.doerf.feeder.viewer.entities.AccountState
import li.doerf.feeder.viewer.entities.Role
import li.doerf.feeder.viewer.entities.User
import li.doerf.feeder.viewer.exception.HttpException
import li.doerf.feeder.viewer.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val jwtTokenProvider: JwtTokenProvider,
        private val authenticationManager: AuthenticationManager,
        private val mailService: MailService
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
        val encodedPassword = passwordEncoder.encode(password)
        if (userRepository.findByUsername(username).isEmpty) {
            val token = UUID.randomUUID().toString()
            val tokenExpiratration = Instant.now().plus(60, ChronoUnit.MINUTES)
            val user = User(0, username, encodedPassword, mutableListOf(Role.ROLE_CLIENT), token, tokenExpiratration, AccountState.ConfirmationPending)
            userRepository.save(user)
            log.info("created user $user")
            GlobalScope.launch {
                mailService.sendSignupMail(user)
            }
            log.debug("mail sent")
            return UserResponseDto(token = jwtTokenProvider.createToken(username, listOf(Role.ROLE_CLIENT)))
        } else {
            log.warn("username already in use")
            throw HttpException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY)
            // TODO ignore this case and send warning email to user
        }
    }

    fun confirm(token: String) {
        log.debug("confirming usernameusing token $token")
        val users = userRepository.findAllByTokenAndState(token, AccountState.ConfirmationPending)
        if(users.isEmpty()) {
            throw HttpException("No user with token found", HttpStatus.BAD_REQUEST)
        } else if (users.size > 1) {
            throw IllegalStateException("more than one user found with token")
        }

        val user = users[0]
        log.debug("found matching user $user")

        if (user.tokenExpiration!!.isBefore(Instant.now())) {
            throw HttpException("token has expired", HttpStatus.PRECONDITION_FAILED)
        }

        if (user.token != token) {
            throw HttpException("Token does not match", HttpStatus.BAD_REQUEST)
        }

        confirmUserToken(user)
        log.info("user account confirmed $user")
    }

    private fun confirmUserToken(user: User) {
        user.token = null
        user.tokenExpiration = null
        user.state = AccountState.Confirmed
        userRepository.save(user)
    }

//    fun delete(username: String) {
//        val user = userRepository.findByUsername(username).orElseThrow { throw IllegalArgumentException("Invalid username $username") }
//        userRepository.delete(user)
//    }

}