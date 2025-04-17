package com.coded.spring.ordering.users

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import jakarta.inject.Named

@Named
interface UserRepository : JpaRepository<UserEntity, Long>{
    fun findByUsername(userName: String): UserEntity?
    fun existsByUsername(username: String): Boolean
}

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val username: String,
    val password: String,

    @Enumerated(EnumType.STRING)
    val role: Roles = Roles.USER
){
    constructor() : this(null, "", "")
}

enum class Roles {
    USER, ADMIN
}