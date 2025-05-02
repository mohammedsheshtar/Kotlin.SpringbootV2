package com.coded.spring.ordering.orders

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name="MenuAPI")
@RestController
class OnlineOrderingController(
    private val onlineOrderingService: OnlineOrderingService
) {

    @GetMapping("/orders")
    fun getOrders(request: HttpServletRequest): List<OrderResponseDTO> {
        val userId = request.getAttribute("userId") as Long
        return onlineOrderingService.getOrders(userId)
    }

    @PostMapping("/orders/add")
    fun addOrders(request: HttpServletRequest, @RequestBody body: RequestOrder): ResponseEntity<Any>{
        val userId = request.getAttribute("userId") as Long
        return onlineOrderingService.addOrders(userId ,body)
    }
}



// the DTO (Data Transfer Object) for our orders and items list
data class RequestItem(
    val name: String,
    val price: Double
)

data class RequestOrder(
    val userId: Long,
    val restaurant: String,
    val items: List<RequestItem>
)

data class OrderResponseDTO(
    val orderId: Long,
    val restaurant: String,
    val items: List<RequestItem>,
    val timeOrdered: String?
)
