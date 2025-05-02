package com.coded.spring.authentication


import com.coded.spring.authentication.users.UserEntity
import com.coded.spring.authentication.users.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user : UserEntity = userRepository.findByUsername(username) ?:
        throw UsernameNotFoundException("User not found...")

        return User.builder()
            .username(user.username)
            .password(user.password)
            .build()
    }
}
