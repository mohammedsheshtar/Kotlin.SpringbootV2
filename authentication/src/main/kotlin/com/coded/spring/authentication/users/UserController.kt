package com.coded.spring.authentication.users


import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@Tag(name="UserAPI")
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

