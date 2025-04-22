package com.coded.spring.ordering

import com.coded.spring.ordering.users.CreateUserRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@Test
	fun helloWorld() {
		val result = restTemplate.getForEntity("/hello", String::class.java)
		assertEquals(expected = HttpStatus.OK, actual = result?.statusCode)
		assertEquals(expected = "Hello World", actual = result.body)

	}

@Test
	fun `Adding user with correct paramter should work`() {
		val request = CreateUserRequest(username = "mohammed111234", password = "1234567")
		val result = restTemplate.postForEntity("/register", request, String::class.java)
		assertEquals(HttpStatus.OK, result.statusCode)
	}

}
