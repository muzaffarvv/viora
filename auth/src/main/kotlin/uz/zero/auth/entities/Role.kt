package uz.zero.auth.entities

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role (

    @Column(nullable = false, unique = true, length = 20)
    var code: String,

    @Column(nullable = false, length = 50)
    var name: String,

    ): BaseEntity()