package li.doerf.feeder.common.entities

import javax.persistence.*

@Entity
@Table(name = "appluser")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val pkey: Long,
        // TODO security - enable
        // @Size(min = 4, max = 255, message = "Minimum username length: 4 characters")val username: String,
        // TODO security - make unique
        val username: String,
        // TODO security - store bcrypt
        var password: String,
        @Enumerated(EnumType.STRING)
        @ElementCollection(fetch = FetchType.EAGER)
        var roles: MutableList<Role>
        // TODO add createdAt, updatedAt properties
)