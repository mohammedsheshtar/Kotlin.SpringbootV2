package com.coded.spring.ordering.profiles


import com.coded.spring.ordering.users.UserEntity
import jakarta.inject.Named
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Named
interface ProfileRepository : JpaRepository<ProfileEntity, Long> {
    fun findByUserId(userId: UserEntity): ProfileEntity?
}

@Entity
@Table(name = "profiles")
data class ProfileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "user_id")
    val userId: UserEntity,

    @Column(name = "first_name")
    val firstName: String,

    @Column(name = "last_name")
    val lastName: String,

    @Column(name = "phone_number")
    val phoneNumber: String
) {
    constructor() : this(null, UserEntity(), "", "", "")
}