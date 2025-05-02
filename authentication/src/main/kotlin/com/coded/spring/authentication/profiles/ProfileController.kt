package com.coded.spring.authentication.profiles


import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User

@Tag(name="ProfileAPI")
@RestController
class ProfileController(
    private val profileService: ProfileService
) {
    @PostMapping("/profile")
    fun addProfile( @AuthenticationPrincipal user: User, @RequestBody request: RequestProfileDTO): ResponseEntity<Any> {
        return profileService.createProfile(username = user.username, request = request)
    }
}

data class RequestProfileDTO(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
)


