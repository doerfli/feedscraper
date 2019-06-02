package li.doerf.feeder.viewer.security

import li.doerf.feeder.viewer.HttpException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtTokenFilter @Autowired constructor(
        private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {


    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse, filterChain: FilterChain) {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                val auth = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = auth
            }
            // TODO security - else ?
        } catch (ex: HttpException) {
            // TODO security - log?
            //this is very important, since it guarantees the user is not authenticated at all
            SecurityContextHolder.clearContext()
            httpServletResponse.sendError(ex.httpStatus.value(), ex.message)
            return
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse)
    }
}