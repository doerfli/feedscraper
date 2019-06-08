package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.common.entities.Role
import li.doerf.feeder.common.entities.User
import li.doerf.feeder.common.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty)
    }

    @Test
    fun testSignupAlreadyExists() {
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
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity)
    }

    @Test
    fun testSignin() {
        val user = User(0, "test@test123.com", passwordEncoder.encode("12345678"), mutableListOf(Role.ROLE_CLIENT))
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
        val user = User(0, "test@test123.com", passwordEncoder.encode("12345678"), mutableListOf(Role.ROLE_CLIENT))
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
        val user = User(0, "test@test123.com", passwordEncoder.encode("12345678"), mutableListOf(Role.ROLE_CLIENT))
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
}