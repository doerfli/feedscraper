package li.doerf.feeder.viewer.entities

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "appluser")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val pkey: Long,
        @Column(unique=true)
        val username: String,
        var password: String,
        @Enumerated(EnumType.STRING)
        @ElementCollection(fetch = FetchType.EAGER)
        var roles: MutableList<Role>,
        var token: String?,
        var tokenExpiration: Instant?,
        @Enumerated(EnumType.STRING)
        val state: AccountState
        // TODO add createdAt, updatedAt properties
)