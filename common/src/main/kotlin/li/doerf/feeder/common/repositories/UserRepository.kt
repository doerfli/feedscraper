package li.doerf.feeder.common.repositories

import li.doerf.feeder.common.entities.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, Long> {

    fun findByUsername(username: String): Optional<User>
}