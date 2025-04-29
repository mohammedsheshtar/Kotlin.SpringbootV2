package com.coded.spring.ordering

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*


@RestController
@Tag(name = "WelcomeAPI")
class WelcomeController(
    @Value("\${company-name}")
    private val companyName: String,
    @Value("\${feature.festive.enabled}")
    private val festiveIsEnabled: Boolean
) {
    @GetMapping("/welcome")
    fun greetUser() = if(!festiveIsEnabled){
        "Welcome to online ordering by $companyName"
    } else {
        "Eidkom Mubarak!"
    }

}