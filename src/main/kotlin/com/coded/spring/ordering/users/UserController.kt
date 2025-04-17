package com.coded.spring.ordering.users


import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class UserController(
    private val userService : UserService
) {
    @PostMapping("register")
    fun registerUser(@RequestBody request: CreateUserRequest): ResponseEntity<Any> {
        return userService.registerUser(request)
    }
}

data class CreateUserRequest(
    @field:NotBlank(message = "Username is required")
    val username: String,

    @field:NotBlank(message = "Password is required")
    val password: String
)

