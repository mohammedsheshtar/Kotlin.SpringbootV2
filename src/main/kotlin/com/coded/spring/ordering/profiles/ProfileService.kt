package com.coded.spring.ordering.profiles


import com.coded.spring.ordering.users.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository
) {
    fun createProfile(username: String, request: RequestProfileDTO): ResponseEntity<Any> {
        val user = userRepository.findByUsername(username)
            ?: return ResponseEntity.badRequest().body(mapOf("error" to "username was not found"))

        if(request.firstName.any { it.isDigit() }) {
            return ResponseEntity.badRequest().body(mapOf("error" to "first name must not contain any numbers"))
        }

        if(request.lastName. any { it.isDigit() }) {
            return ResponseEntity.badRequest().body(mapOf("error" to "last name must not contain any numbers"))
        }

        if(!request.phoneNumber.matches(Regex("^\\d{8}$"))) {
            return ResponseEntity.badRequest().body(mapOf("error" to "phone number must be 8 digits"))
        }

        val existingProfile = profileRepository.findByUserId(user)

        val profile = if (existingProfile != null) {
            existingProfile.copy(
                firstName = request.firstName,
                lastName = request.lastName,
                phoneNumber = request.phoneNumber
            )
        } else {
            ProfileEntity(
                userId = user,
                firstName = request.firstName,
                lastName = request.lastName,
                phoneNumber = request.phoneNumber
            )
        }

        profileRepository.save(profile)
        return ResponseEntity.ok().build()
    }
}
