package li.doerf.feeder.common.entities

import javax.persistence.*

@Entity
data class ItemState(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val pkey: Long,
        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User,
        @ManyToOne
        @JoinColumn(name = "item_id")
        val item: Item,
        @ManyToOne
        @JoinColumn(name = "feed_id")
        val feed: Feed,
        var isRead: Boolean
)