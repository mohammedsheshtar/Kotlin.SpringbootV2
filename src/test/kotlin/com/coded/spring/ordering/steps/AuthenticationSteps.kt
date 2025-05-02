package com.coded.spring.ordering.steps

import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals

class AuthSteps {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var jwtService: com.coded.spring.ordering.authentication.jwt.JwtService

    private lateinit var headers: HttpHeaders
    private lateinit var response: ResponseEntity<String>

    @Given("a valid JWT token for user {string}")
    fun givenAValidJwtToken(username: String) {
        val token = jwtService.generateToken(username)
        headers = HttpHeaders()
        headers.set("Authorization", "Bearer $token")
    }

    @When("I send a GET request to {string} with the token")
    fun iSendAGetRequestWithToken(endpoint: String) {
        val request = HttpEntity<String>(headers)
        response = restTemplate.exchange(endpoint, HttpMethod.GET, request, String::class.java)
    }

    @Then("the response status should be {int}")
    fun theResponseStatusShouldBe(expectedStatus: Int) {
        assertEquals(expectedStatus, response.statusCode.value())
    }

    @Then("the response body should be {string}")
    fun theResponseBodyShouldBe(expectedBody: String) {
        assertEquals(expectedBody, response.body)
    }
}
