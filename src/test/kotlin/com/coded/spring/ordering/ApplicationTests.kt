package com.coded.spring.ordering

import com.coded.spring.ordering.authentication.jwt.JwtService
import com.coded.spring.ordering.menus.MenuDTO
import com.coded.spring.ordering.orders.OrderResponseDTO
import com.coded.spring.ordering.orders.RequestItem
import com.coded.spring.ordering.orders.RequestOrder
import com.coded.spring.ordering.profiles.RequestProfileDTO
import com.coded.spring.ordering.users.CreateUserRequest
import com.coded.spring.ordering.users.UserEntity
import com.coded.spring.ordering.users.UserRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.util.MultiValueMap
import java.math.BigDecimal
import kotlin.test.assertEquals

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	properties = ["src/test/resources/application-test.properties"]
)
@ActiveProfiles("test")
class ApplicationTests {

	companion object {
		lateinit var savedUser: UserEntity
		@JvmStatic
		@BeforeAll
		fun setUp(
			@Autowired userRepository: UserRepository,
			@Autowired passwordEncoder: PasswordEncoder
		) {
			userRepository.deleteAll()
			val user = UserEntity(
				username = "momo1234",
				password = passwordEncoder.encode("123dB45")
			)
			savedUser = userRepository.save(user)
		}
	}

	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@Test
	fun `test hello endpoint with JWT`(@Autowired jwtService: JwtService) {
		val token = jwtService.generateToken("momo1234")
		val headers = HttpHeaders(
			MultiValueMap.fromSingleValue(mapOf("Authorization" to "Bearer $token"))
		)
		val request = HttpEntity<String>(headers)

		val result = restTemplate.exchange(
			"/hello",
			HttpMethod.GET,
			request,
			String::class.java
		)

		assertEquals(HttpStatus.OK, result.statusCode)
		assertEquals("Hello World", result.body)
	}

	@Test
	fun `adding a new user with correct parameters should work`() {
		val request = CreateUserRequest(username = "mohammed67234", password = "12Ln34567")
		val result = restTemplate.postForEntity("/register", request, String::class.java)
		assertEquals(HttpStatus.OK, result.statusCode)
	}

	@Test
	fun `adding a new user with with username already existing should NOT work`() {
		val request = CreateUserRequest(username = "mohammed67234", password = "12Ln34567")
		val result = restTemplate.postForEntity("/register", request, String::class.java)
		assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
		assertEquals(
			"""{"error":"username ${request.username} already exists"}""",
			result.body
		)
	}

	@Test
	fun `adding a new user with with password having less than six chars should NOT work`() {
		val request = CreateUserRequest(username = "mohammed6734234", password = "12")
		val result = restTemplate.postForEntity("/register", request, String::class.java)
		assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
		assertEquals(
			"""{"error":"password must be at least 6 characters"}""",
			result.body
		)
	}

	@Test
	fun `adding a new user with with password not having a capital letter should NOT work`() {
		val request = CreateUserRequest(username = "mohamed672345324", password = "1234567n")
		val result = restTemplate.postForEntity("/register", request, String::class.java)
		assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
		assertEquals(
			"""{"error":"password must have at least one capital letter"}""",
			result.body
		)
	}

	@Test
	fun `adding a new user with with password not having a digit should NOT work`() {
		val request = CreateUserRequest(username = "mohammmed67234", password = "Mohammedss")
		val result = restTemplate.postForEntity("/register", request, String::class.java)
		assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
		assertEquals(
			"""{"error":"password must have at least one digit"}""",
			result.body
		)
	}

	@Test
	fun `adding a new order with correct parameters should work`(@Autowired jwtService: JwtService) {

		val token = jwtService.generateToken("momo1234")
		val headers = HttpHeaders(
			MultiValueMap.fromSingleValue(mapOf("Authorization" to "Bearer $token"))
		)

		val body = RequestOrder(
			userId = savedUser.id!!,
			restaurant = "WK",
			items = listOf(RequestItem("Nuggies", 3.950))
		)

		val requestEntity = HttpEntity<RequestOrder>(body, headers)
		val actualResponse = restTemplate.exchange(
			"/orders/add", //Endpoint
			HttpMethod.POST,
			requestEntity,
			OrderResponseDTO::class.java
		)

		assertEquals(HttpStatus.OK, actualResponse.statusCode)

		val responseBody = actualResponse.body!!
		assertEquals("momo1234", responseBody.username)
		assertEquals("WK", responseBody.restaurant)
		assertEquals(listOf(RequestItem("Nuggies", 3.950)), responseBody.items)

		val now = System.currentTimeMillis()
		val orderTime = java.time.LocalDateTime
			.parse(responseBody.timeOrdered)
			.atZone(java.time.ZoneId.systemDefault())
			.toInstant()
			.toEpochMilli()
		assert(orderTime <= now && orderTime > now - 1000) {
			"Expected timeOrdered to be recent. Got: ${responseBody.timeOrdered}"
		}


	}

	@Test
	fun `adding a new order with incorrect user id should NOT work`(@Autowired jwtService: JwtService) {

		val token = jwtService.generateToken("momo1234")
		val headers = HttpHeaders(
			MultiValueMap.fromSingleValue(mapOf("Authorization" to "Bearer $token"))
		)

		val body = RequestOrder(
			userId = 900,
			restaurant = "WK",
			items = listOf(RequestItem("Nuggies", 3.950))
		)

		val requestEntity = HttpEntity<RequestOrder>(body, headers)
		val actualResponse = restTemplate.exchange(
			"/orders/add", //Endpoint
			HttpMethod.POST,
			requestEntity,
			String::class.java
		)

		assertEquals(HttpStatus.BAD_REQUEST, actualResponse.statusCode)
		assertEquals(
			"""{"error":"user with ID ${requestEntity.body?.userId} was not found"}""",
			actualResponse.body
		)
	}

	@Test
	fun `adding a new order with negative item price should NOT work`(@Autowired jwtService: JwtService) {

		val token = jwtService.generateToken("momo1234")
		val headers = HttpHeaders(
			MultiValueMap.fromSingleValue(mapOf("Authorization" to "Bearer $token"))
		)

		val body = RequestOrder(
			userId = savedUser.id!!,
			restaurant = "WK",
			items = listOf(RequestItem("Nuggies", -3.950))
		)

		val requestEntity = HttpEntity<RequestOrder>(body, headers)
		val actualResponse = restTemplate.exchange(
			"/orders/add", //Endpoint
			HttpMethod.POST,
			requestEntity,
			String::class.java
		)

		assertEquals(HttpStatus.BAD_REQUEST, actualResponse.statusCode)
		assertEquals(
			"""{"error":"item price cannot be negative"}""",
			actualResponse.body
		)

	}

	@Test
	fun `adding new profile should work`(@Autowired jwtService: JwtService) {
		val token = jwtService.generateToken("momo1234")
		val headers = HttpHeaders()
		headers.set("Authorization", "Bearer $token")

		val requestBody = RequestProfileDTO(
			firstName = "Mohammed",
			lastName = "Sheshtar",
			phoneNumber = "12345678"
		)

		val entity = HttpEntity(requestBody, headers)

		val response = restTemplate.exchange(
			"/profile",
			HttpMethod.POST,
			entity,
			String::class.java
		)

		assertEquals(HttpStatus.OK, response.statusCode)
	}

	@Test
	fun `adding new profile with first name having numbers should NOT work`(@Autowired jwtService: JwtService) {
		val token = jwtService.generateToken("momo1234")
		val headers = HttpHeaders()
		headers.set("Authorization", "Bearer $token")

		val requestBody = RequestProfileDTO(
			firstName = "Mohammed111",
			lastName = "Sheshtar",
			phoneNumber = "12345678"
		)

		val entity = HttpEntity(requestBody, headers)

		val response = restTemplate.exchange(
			"/profile",
			HttpMethod.POST,
			entity,
			String::class.java
		)

		assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
		assertEquals(
			"""{"error":"first name must not contain any numbers"}""",
			response.body
		)
	}

	@Test
	fun `adding new profile with last name having numbers should NOT work`(@Autowired jwtService: JwtService) {
		val token = jwtService.generateToken("momo1234")
		val headers = HttpHeaders()
		headers.set("Authorization", "Bearer $token")

		val requestBody = RequestProfileDTO(
			firstName = "Mohammed",
			lastName = "Sheshtar222",
			phoneNumber = "12345678"
		)

		val entity = HttpEntity(requestBody, headers)

		val response = restTemplate.exchange(
			"/profile",
			HttpMethod.POST,
			entity,
			String::class.java
		)

		assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
		assertEquals(
			"""{"error":"last name must not contain any numbers"}""",
			response.body
		)
	}

	@Test
	fun `adding new profile with phone number not being eight digits should NOT work`(@Autowired jwtService: JwtService) {
		val token = jwtService.generateToken("momo1234")
		val headers = HttpHeaders()
		headers.set("Authorization", "Bearer $token")

		val requestBody = RequestProfileDTO(
			firstName = "Mohammed",
			lastName = "Sheshtar",
			phoneNumber = "12378"
		)

		val entity = HttpEntity(requestBody, headers)

		val response = restTemplate.exchange(
			"/profile",
			HttpMethod.POST,
			entity,
			String::class.java
		)

		assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
		assertEquals(
			"""{"error":"phone number must be 8 digits"}""",
			response.body
		)
	}

	@Test
	fun `fetching list of orders should work`(@Autowired jwtService: JwtService) {
		val token = jwtService.generateToken("momo1234")
		val headers = HttpHeaders()
		headers.set("Authorization", "Bearer $token")

		val request = HttpEntity<String>(headers)

		val result = restTemplate.exchange(
			"/orders",
			HttpMethod.GET,
			request,
			String::class.java
		)

		assertEquals(HttpStatus.OK, result.statusCode)
	}

	@Test
	fun `adding a menu should work`(@Autowired jwtService: JwtService) {
		val token = jwtService.generateToken("momo1234")
		val headers = HttpHeaders()
		headers.set("Authorization", "Bearer $token")

		val requestBody = MenuDTO(
			name = "Hamburga",
			price = BigDecimal.TWO
		)

		val entity = HttpEntity(requestBody, headers)

		val response = restTemplate.exchange(
			"/menus",
			HttpMethod.POST,
			entity,
			String::class.java
		)

		assertEquals(HttpStatus.OK, response.statusCode)
	}

	@Test
	fun `fetching list of menus should work`(@Autowired jwtService: JwtService) {
		val token = jwtService.generateToken("momo1234")
		val headers = HttpHeaders()
		headers.set("Authorization", "Bearer $token")

		val request = HttpEntity<String>(headers)

		val result = restTemplate.exchange(
			"/menus",
			HttpMethod.GET,
			request,
			String::class.java
		)

		assertEquals(HttpStatus.OK, result.statusCode)
	}
}
