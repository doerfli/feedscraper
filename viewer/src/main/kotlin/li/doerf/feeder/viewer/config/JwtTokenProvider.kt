package li.doerf.feeder.viewer.config

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import li.doerf.feeder.viewer.entities.Role
import li.doerf.feeder.viewer.exception.HttpException
import li.doerf.feeder.viewer.services.UserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import javax.annotation.PostConstruct
import javax.crypto.spec.SecretKeySpec
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider @Autowired constructor(
        private val userDetailsService: UserDetailsService
){

    private lateinit var jwtKey: SecretKeySpec
    @Value("\${security.jwt.token.secret-key}")
    private lateinit var secretKey: String

    @Value("\${security.jwt.token.expire-length:3600000}")
    private val validityInMilliseconds: Long = 3600000 // 1h

    @PostConstruct
    fun init() {
        val bytes = secretKey.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        jwtKey = SecretKeySpec(bytes, SignatureAlgorithm.HS256.jcaName)
    }

    fun createJwtToken(username: String, roles: List<Role>): String {

        val claims = Jwts.claims().setSubject(username)
        claims["auth"] = roles.map { it.authority }.toList()

        val now = Instant.now()
        val validity = now.plusMillis(validityInMilliseconds)

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(validity))
                .signWith(jwtKey)
                .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUsername(token: String): String {
        return Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).body.subject
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token)
            return true
        } catch (e: JwtException) {
            throw HttpException("Expired or invalid JWT token", HttpStatus.FORBIDDEN)
        } catch (e: IllegalArgumentException) {
            throw HttpException("Expired or invalid JWT token", HttpStatus.FORBIDDEN)
        }

    }

}