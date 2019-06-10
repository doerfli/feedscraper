package li.doerf.feeder.viewer.util

import li.doerf.feeder.common.entities.User
import li.doerf.feeder.common.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserUtils @Autowired constructor(
        val userRepository: UserRepository
) {

//    companion object {
//        @Suppress("JAVA_CLASS_ON_COMPANION")
//        private val log = getLogger(javaClass)
//    }

    fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        return userRepository.findByUsername(authentication.name).orElseThrow { IllegalArgumentException("user does not exist: ${authentication.name}") }
    }
}
