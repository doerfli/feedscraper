package li.doerf.feeder.viewer.controllers

import li.doerf.feeder.viewer.entities.AccountState
import li.doerf.feeder.viewer.entities.User
import li.doerf.feeder.viewer.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TestHelper @Autowired constructor(
        val userRepository: UserRepository
){
    fun createUser( username: String): User {
        val user = User(0, username, "password", mutableListOf(),  null, null, AccountState.Confirmed)
        userRepository.save(user)
        return user
    }
}