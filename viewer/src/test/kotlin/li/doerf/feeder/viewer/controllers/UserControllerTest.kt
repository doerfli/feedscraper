package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.viewer.entities.AccountState
import li.doerf.feeder.viewer.entities.Role
import li.doerf.feeder.viewer.entities.User
import li.doerf.feeder.viewer.repositories.UserRepository
import li.doerf.feeder.viewer.services.MailService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension::class)
class UserControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @MockBean
    private lateinit var mailService: MailService

    @Test
    fun testSignup() {
        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/users/signup")
                .content("""
                    {
                        "username":"test@test123.com",
                        "password": "12345678"
                    }
                """)
                .contentType(MediaType.APPLICATION_JSON))

        // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").doesNotExist())
    }

    @Test
    fun testSignupExistsNotConfirmed() {
        // given
        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/users/signup")
                .content("""
                    {
                        "username":"test@test123.com",
                        "password": "12345678"
                    }
                """)
                .contentType(MediaType.APPLICATION_JSON))

        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/users/signup")
                .content("""
                {
                    "username":"test@test123.com",
                    "password": "12345678"
                }
            """)
                .contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun testSignupExistsConfirmed() {
        // given
        val username = "test@test123.com"
        mvc.perform(MockMvcRequestBuilders.post("/api/users/signup")
                .content("""
                    {
                        "username":"$username",
                        "password": "12345678"
                    }
                """)
                .contentType(MediaType.APPLICATION_JSON))

        val user = userRepository.findByUsername(username).orElseThrow()
        assertThat(user.token).isNotNull()

        mvc.perform(MockMvcRequestBuilders.get("/api/users/confirm/${user.token}")
                .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)

        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/users/signup")
                .content("""
                {
                    "username":"$username",
                    "password": "12345678"
                }
            """)
                .contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun testSignupAndConfirm() {
        val username = "test@test123.com"
        mvc.perform(MockMvcRequestBuilders.post("/api/users/signup")
                .content("""
                    {
                        "username": "$username",
                        "password": "12345678"
                    }
                """)
                .contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").doesNotExist())

        val user = userRepository.findByUsername(username).orElseThrow()
        assertThat(user.token).isNotNull()

        mvc.perform(MockMvcRequestBuilders.get("/api/users/confirm/${user.token}")
                .contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty)
    }

    @Test
    fun testSignin() {
        val user = User(0, "test@test123.com", passwordEncoder.encode("12345678"), mutableListOf(Role.ROLE_CLIENT),null, null, AccountState.Confirmed)
        userRepository.save(user)

        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/users/signin")
                .content("""
                    {
                        "username":"test@test123.com",
                        "password": "12345678"
                    }
                """)
                .contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty)
    }

    @Test
    fun testSigninUsernameDoesNotExist() {
        val user = User(0, "test@test123.com", passwordEncoder.encode("12345678"), mutableListOf(Role.ROLE_CLIENT), null, null, AccountState.Confirmed)
        userRepository.save(user)

        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/users/signin")
                .content("""
                    {
                        "username":"test1@test123.com",
                        "password": "12345678"
                    }
                """)
                .contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity)
    }

    @Test
    fun testSigninInvalidPassword() {
        val user = User(0, "test@test123.com", passwordEncoder.encode("12345678"), mutableListOf(Role.ROLE_CLIENT), null, null, AccountState.Confirmed)
        userRepository.save(user)

        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/users/signin")
                .content("""
                    {
                        "username":"test@test123.com",
                        "password": "12345677"
                    }
                """)
                .contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity)
    }

    @Test
    fun testSigninAccountNotConfirmed() {
        val user = User(0, "test@test123.com", passwordEncoder.encode("12345678"), mutableListOf(Role.ROLE_CLIENT),UUID.randomUUID().toString(), Instant.now().plus(50, ChronoUnit.MINUTES), AccountState.ConfirmationPending)
        userRepository.save(user)

        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/users/signin")
                .content("""
                    {
                        "username":"test@test123.com",
                        "password": "12345678"
                    }
                """)
                .contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity)
    }

    @Test
    fun testRequestPasswordReset() {
        // given
        val user = User(0, "test@test123.com", passwordEncoder.encode("12345678"), mutableListOf(Role.ROLE_CLIENT),null, null, AccountState.Confirmed)
        userRepository.save(user)

        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/users/passwordReset")
                .content("""
                    {
                        "username":"test@test123.com"
                    }
                """)
                .contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun testPasswordReset() {
        val user = User(0, "test@test123.com", passwordEncoder.encode("12345678"), mutableListOf(Role.ROLE_CLIENT),
                "AAAAAA", Instant.now().plus(60, ChronoUnit.MINUTES), AccountState.PasswordResetRequested)
        userRepository.save(user)

        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/users/passwordReset/AAAAAA")
                .content("""
                    {
                        "password": "newpassword"
                    }
                """)
                .contentType(MediaType.APPLICATION_JSON))

                // then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

}