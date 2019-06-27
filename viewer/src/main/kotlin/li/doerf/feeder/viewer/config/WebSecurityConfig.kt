package li.doerf.feeder.viewer.config

import li.doerf.feeder.common.util.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig @Autowired constructor(
        private val jwtTokenProvider: JwtTokenProvider
): WebSecurityConfigurerAdapter() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @Value("\${security.bcrypt.encoder.strength:13}")
    private val bcryptEncoderStrength: Int = 13

    override fun configure(http: HttpSecurity) {
        // Disable CSRF (cross site request forgery)
        http.csrf().disable()

        http.cors()

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // Entry points
        http.authorizeRequests()//
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll() // allow preflight CORS requests
                .antMatchers("/api/users/signin").permitAll()
                .antMatchers("/api/users/signup").permitAll()
                .antMatchers("/api/users/confirm/**").permitAll()
                .antMatchers("/ws/**").permitAll() // websockets are authenticated via connect interceptor in WebSocketConfig
                .anyRequest().authenticated()

        http.exceptionHandling().accessDeniedPage("/login")
        http.apply(JwtTokenFilterConfigurer(jwtTokenProvider))

        // if you want to test the API from a browser
        // http.httpBasic();
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(bcryptEncoderStrength)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

}