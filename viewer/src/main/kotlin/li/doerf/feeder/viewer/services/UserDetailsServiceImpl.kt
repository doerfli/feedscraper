package li.doerf.feeder.viewer.services

import li.doerf.feeder.viewer.entities.AccountState
import li.doerf.feeder.viewer.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl @Autowired constructor(
        val userRepository: UserRepository
): UserDetailsService, li.doerf.feeder.viewer.services.UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username).orElseThrow{ throw UsernameNotFoundException("User '$username' not found") }

        return User
                .withUsername(username)
                .password(user.password)
                .authorities(user.roles)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(user.state == AccountState.ConfirmationPending)
                .build()
    }

}