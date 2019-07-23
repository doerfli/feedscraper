package li.doerf.feeder.viewer.services

import org.springframework.security.core.userdetails.UserDetails

interface UserDetailsService {
    fun loadUserByUsername(username: String): UserDetails
}