package li.doerf.feeder.common.entities

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
        var roles: MutableList<Role>
        // TODO add createdAt, updatedAt properties
)