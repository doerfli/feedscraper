package li.doerf.feeder.viewer.services

import li.doerf.feeder.viewer.config.JwtTokenProvider
import li.doerf.feeder.viewer.entities.AccountState
import li.doerf.feeder.viewer.entities.Role
import li.doerf.feeder.viewer.entities.User
import li.doerf.feeder.viewer.exception.HttpException
import li.doerf.feeder.viewer.repositories.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@Import(UserService::class)
@DataJpaTest
class UserServiceTest {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var userService: UserService
    @MockBean
    private lateinit var mailService: MailService
    @MockBean
    private lateinit var passwordEncoder: PasswordEncoder
    @MockBean
    private lateinit var jwtTokenProvider: JwtTokenProvider
    @MockBean
    private lateinit var authenticationManager: AuthenticationManager

    @Test
    fun testConfirm() {
        val tok = UUID.randomUUID().toString()
        val expiration = Instant.now().plus(60, ChronoUnit.MINUTES)
        val user = User(0, "someone@test.com", "aaaaaaaa", mutableListOf(Role.ROLE_CLIENT),
                tok, expiration, AccountState.ConfirmationPending)
        userRepository.save(user)

        Mockito.`when`(jwtTokenProvider.createJwtToken(Mockito.anyString(), Mockito.anyList())).thenReturn("jwttokenbladiblubbb")

        val jwtToken = userService.confirm(tok)
        assertThat(jwtToken).isEqualTo("jwttokenbladiblubbb")

        val userAfter = userRepository.findById(user.pkey).get()
        assertThat(userAfter.token).isNull()
        assertThat(userAfter.tokenExpiration).isNull()
        assertThat(userAfter.state).isEqualTo(AccountState.Confirmed)
    }

    @Test
    fun testConfirm_AlreadyConfirmed() {
        val token = UUID.randomUUID().toString()
        val expiration = Instant.now().plus(60, ChronoUnit.MINUTES)
        val user = User(0, "someone@test.com", "aaaaaaaa", mutableListOf(Role.ROLE_CLIENT),
                null, null, AccountState.Confirmed)
        userRepository.save(user)

        assertThatThrownBy {
            userService.confirm(token)
        }.isInstanceOf(HttpException::class.java)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST)
    }

    @Test
    fun testConfirm_TokenNotMatching() {
        val token = UUID.randomUUID().toString()
        val expiration = Instant.now().plus(60, ChronoUnit.MINUTES)
        val user = User(0, "someone@test.com", "aaaaaaaa", mutableListOf(Role.ROLE_CLIENT),
                token, expiration, AccountState.ConfirmationPending)
        userRepository.save(user)

        assertThatThrownBy {
            userService.confirm(UUID.randomUUID().toString())
        }.isInstanceOf(HttpException::class.java)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST)

        val userAfter = userRepository.findById(user.pkey).get()
        assertThat(userAfter.token).isNotNull()
        assertThat(userAfter.tokenExpiration).isNotNull()
        assertThat(userAfter.state).isEqualTo(AccountState.ConfirmationPending)
    }

    @Test
    fun testConfirm_TokenExpired() {
        val token = UUID.randomUUID().toString()
        val expiration = Instant.now().minus(60, ChronoUnit.MINUTES)
        val user = User(0, "someone@test.com", "aaaaaaaa", mutableListOf(Role.ROLE_CLIENT),
                token, expiration, AccountState.ConfirmationPending)
        userRepository.save(user)

        assertThatThrownBy {
            userService.confirm(token)
        }.isInstanceOf(HttpException::class.java)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.PRECONDITION_FAILED)

        val userAfter = userRepository.findById(user.pkey).get()
        assertThat(userAfter.token).isNotNull()
        assertThat(userAfter.tokenExpiration).isNotNull()
        assertThat(userAfter.state).isEqualTo(AccountState.ConfirmationPending)
    }
}