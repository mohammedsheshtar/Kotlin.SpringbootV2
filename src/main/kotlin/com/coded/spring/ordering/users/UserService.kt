package com.coded.spring.ordering.users

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

    @Service
    class UserService(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder
    ) {
        fun registerUser(request: CreateUserRequest): ResponseEntity<Any> {
            if (userRepository.existsByUsername(request.username)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(mapOf("error" to "username ${request.username} already exists"))
            }

            if(request.password.length < 6) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(mapOf("error" to "password must be at least 6 characters"))
            }

            if(!request.password.any { it.isUpperCase() }) {
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "password must have at least one capital letter"))
            }

            if(!request.password.any { it.isDigit() }) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "password must have at least one digit"))
            }

                val hashedPassword = passwordEncoder.encode(request.password)
            val newUser = UserEntity(username = request.username, password = hashedPassword)
            userRepository.save(newUser)

            return ResponseEntity.ok().build()
        }
    }
