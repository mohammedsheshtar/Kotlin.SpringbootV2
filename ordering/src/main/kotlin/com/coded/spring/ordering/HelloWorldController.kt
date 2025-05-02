package com.coded.spring.ordering

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*

@Tag(name="HelloWorldAPI")
@RestController
class HelloWorldController(
    @Value("\${hello-world}")
    val helloWorldMessage: String
) {

    @GetMapping("/hello")
    fun helloWorld() = helloWorldMessage;
}