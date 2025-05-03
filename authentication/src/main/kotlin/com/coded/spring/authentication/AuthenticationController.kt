package com.coded.spring.authentication

import com.coded.spring.authentication.jwt.JwtService
import com.coded.spring.authentication.users.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


@Tag(name="AuthenticationAPI")
@RestController
@RequestMapping("/authentication")
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtService: JwtService,
    private val userService: UserService
) {
    @Tag(name = "AuthenticationAPI")
    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthenticationRequest): AuthenticationResponse {
        val authToken = UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
        val authentication = authenticationManager.authenticate(authToken)

        if (authentication.isAuthenticated) {
            val userDetails = userDetailsService.loadUserByUsername(authRequest.username)
            val token = jwtService.generateToken(userDetails.username)
            return AuthenticationResponse (token)
        } else {
            throw UsernameNotFoundException("Invalid user request!")
        }
    }

    @PostMapping("/check-token")
    fun checkToken(
        principal: Principal
    ): CheckTokenResponse {
        return CheckTokenResponse(
            userId = userService.findByUsername(principal.name)
        )
    }
}


data class AuthenticationRequest(
    val username: String,
    val password: String
)

data class AuthenticationResponse(
    val token: String
)

data class CheckTokenResponse(
    val userId: Long
)

data class RegisterRequest(
    val username: String,
    val password: String
)

data class RegisterFailureResponse(
    val error: AddUserError
)

enum class AddUserError {
    INVALID_USERNAME, TOO_SHORT_PASSWORD, MAX_ACCOUNT_LIMIT_REACHED
}