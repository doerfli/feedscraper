package li.doerf.feeder.viewer.services

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.config.JwtTokenProvider
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

    fun signin(username: String, password: String): String {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
            return jwtTokenProvider.createJwtToken(username, userRepository.findByUsername(username).orElseThrow {throw IllegalArgumentException("invalid username $username")}.roles)
        } catch (e: AuthenticationException) {
            log.warn("could not login user", e)
            val user = userRepository.findByUsername(username)
            if (user.isPresent && user.get().state == AccountState.ConfirmationPending) {
                GlobalScope.launch {
                    mailService.sendSignupReminderMail(user.get())
                }
            }
            throw HttpException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY)
        }
    }

    fun signup(username: String, password: String) {
        // always encode password to avoid timing attacks
        val encodedPassword = passwordEncoder.encode(password)
        val userEntity = userRepository.findByUsername(username)
        if (userEntity.isEmpty) {
            createNewUser(username, encodedPassword)
        } else {
            handleExistingUser(userEntity.get())
        }
    }

    private fun createNewUser(username: String, encodedPassword: String) {
        val user = User(0, username, encodedPassword, mutableListOf(Role.ROLE_CLIENT), null, null, AccountState.ConfirmationPending)
        setConfirmationTokenAndSendMail(user)
        userRepository.save(user)
        log.info("created new user - $user")
    }

    private fun handleExistingUser(user: User) {
        if (user.state == AccountState.ConfirmationPending) {
            // issue new token and resend confirmation email
            setConfirmationTokenAndSendMail(user)
            userRepository.save(user)
            log.info("Updated user with new token - $user ")
        } else {
            log.warn("username already in use - $user")
            GlobalScope.launch {
                mailService.sendSignupAccountExistsMail(user)
            }
        }
    }

    private fun setConfirmationTokenAndSendMail(user: User) {
        var token = generateUniqueToken(10)
        val tokenExpiration = Instant.now().plus(60, ChronoUnit.MINUTES)
        user.token = token
        user.tokenExpiration = tokenExpiration
        GlobalScope.launch {
            mailService.sendSignupMail(user)
        }
    }

    fun generateUniqueToken(length: Int) : String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        var token: String
        do {
            token = (1..length)
                    .map { allowedChars.random() }
                    .joinToString("")
        } while(userRepository.findAllByTokenAndState(token, AccountState.ConfirmationPending).isNotEmpty())
        return token

    }

    fun confirm(token: String): String {
        log.debug("confirming username using Confirmation-Token $token")
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
            throw HttpException("Confirmation-Token does not match", HttpStatus.BAD_REQUEST)
        }

        confirmUserToken(user)
        log.info("user account confirmed $user")

        return jwtTokenProvider.createJwtToken(user.username, user.roles)
    }

    private fun confirmUserToken(user: User) {
        user.token = null
        user.tokenExpiration = null
        user.state = AccountState.Confirmed
        userRepository.save(user)
    }

    fun requestPasswordReset(username: String) {
        log.debug("starting password reset request for $username")
        val userOpt = userRepository.findByUsername(username)
        if (userOpt.isEmpty) {
            log.warn("user not found: $username")
            return
        }

        val user = userOpt.get()
        var token = generateUniqueToken(10)
        val tokenExpiration = Instant.now().plus(60, ChronoUnit.MINUTES)
        user.token = token
        user.tokenExpiration = tokenExpiration
        user.state = AccountState.PasswordResetRequested
        userRepository.save(user)
        GlobalScope.launch {
            mailService.sendPasswordResetMail(user)
        }
    }

    fun resetPassword(token: String, password: String) {
        log.debug("password reset with token $token")
        // always encode password to avoid timing attacks
        val encodedPassword = passwordEncoder.encode(password)
        val users = userRepository.findAllByTokenAndState(token, AccountState.PasswordResetRequested)
        if(users.isEmpty()) {
            throw HttpException("No user with token found", HttpStatus.BAD_REQUEST)
        } else if (users.size > 1) {
            throw IllegalStateException("more than one user found with token")
        }

        val user = users[0]

        if (user.tokenExpiration!!.isBefore(Instant.now())) {
            throw HttpException("token has expired", HttpStatus.PRECONDITION_FAILED)
        }

        if (user.token != token) {
            throw HttpException("Confirmation-Token does not match", HttpStatus.BAD_REQUEST)
        }

        log.debug("reset token is valid")

        user.password = encodedPassword
        user.token = null
        user.tokenExpiration = null
        user.state = AccountState.Confirmed
        userRepository.save(user)
        log.debug("user updated with new password")
    }

}