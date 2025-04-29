package com.coded.spring.ordering

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name="HelloWorldAPI")
@RestController
class HelloWorldController {

    @GetMapping("/hello")
    fun helloWorld() = "Hello World";
}