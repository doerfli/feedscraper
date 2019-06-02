package li.doerf.feeder.viewer.security

import li.doerf.feeder.common.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService @Autowired constructor(
        val userRepository: UserRepository
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username).orElseThrow{ throw UsernameNotFoundException("User '$username' not found") }

        return User
                .withUsername(username)
                .password(user.password) // TOOD security - ???
                .authorities(user.roles)
                .accountExpired(false) // TOOD security - ??? need to map this from database
                .accountLocked(false) // TOOD security - ??? need to map this from database
                .credentialsExpired(false) // TOOD security - ??? need to map this from database
                .disabled(false) // TOOD security - ??? need to map this from database
                .build()
    }

}