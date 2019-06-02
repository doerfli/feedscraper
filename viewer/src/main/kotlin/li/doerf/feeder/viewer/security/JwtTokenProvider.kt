package li.doerf.feeder.viewer.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import li.doerf.feeder.common.entities.Role
import li.doerf.feeder.viewer.HttpException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest


@Component
class JwtTokenProvider @Autowired constructor(
        private val userDetailsService: UserDetailsService
){

    /**
     * TODO security - THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
     * microservices environment, this key would be kept on a config-server.
     */
    @Value("\${security.jwt.token.secret-key:secret-key}")
    private var secretKey: String? = null

    // TODO put in configuration
    @Value("\${security.jwt.token.expire-length:3600000}")
    private val validityInMilliseconds: Long = 3600000 // 1h

    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey!!.toByteArray())
    }

    fun createToken(username: String, roles: List<Role>): String {

        val claims = Jwts.claims().setSubject(username)
        claims["auth"] = roles.map { it.authority }.filterNotNull().toList()

        val now = Date()
        // TODO security use date modifier
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                // TODO security need more?
                // TODO refresh token?
                .compact()
    }

    fun getAuthentication(token: String): Authentication {
        // TODO security - like this?
        val userDetails = userDetailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities())
    }

    fun getUsername(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null // TODO security - really?
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return true
        } catch (e: JwtException) {
            throw HttpException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: IllegalArgumentException) {
            throw HttpException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

}