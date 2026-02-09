package uz.zero.auth.entities

import jakarta.persistence.*

@Entity()
@Table(name = "users")
class User(

    @Column(nullable = false, length = 72)
    var firstName: String,

    @Column(length = 60)
    var lastName: String,

    @Column(nullable = false, unique = true, length = 32)
    var phoneNum: String,

    @Column(nullable = false)
    var password: String,

    var orgId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    var role: Role

) : BaseEntity()

