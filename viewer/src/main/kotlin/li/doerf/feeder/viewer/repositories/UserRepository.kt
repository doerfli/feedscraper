package li.doerf.feeder.viewer.repositories

import li.doerf.feeder.viewer.entities.AccountState
import li.doerf.feeder.viewer.entities.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, Long> {

    fun findByUsername(username: String): Optional<User>
    fun findAllByTokenAndState(username: String, status: AccountState): List<User>
}