package com.coded.spring.ordering

import org.springframework.web.bind.annotation.*

@RestController
class HelloWorldController {

    @GetMapping("/hello")
    fun helloWorld() = "Hello World";
}