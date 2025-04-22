package com.coded.spring.ordering.scripts


import com.coded.spring.ordering.Application
import com.coded.spring.ordering.users.UserEntity
import com.coded.spring.ordering.users.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import com.coded.spring.ordering.users.Roles
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class InitUserRunner {
    @Bean
    fun initUsers(userRepository: UserRepository, passwordEncoder: PasswordEncoder) = CommandLineRunner {
        val user = UserEntity(
            username = "momo1111112",
            password = passwordEncoder.encode("password123"),
            role = Roles.USER
        )
        if (userRepository.findByUsername(user.username) == null) {
            println("Creating user ${user.username}")
            userRepository.save(user)
        } else  {
            println("User ${user.username} already exists")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args).close()
}