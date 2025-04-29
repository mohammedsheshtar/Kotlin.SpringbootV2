package com.coded.spring.ordering.orders

import com.coded.spring.ordering.items.ItemsEntity
import com.coded.spring.ordering.items.ItemsRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name="MenuAPI")
@RestController
class OnlineOrderingController(
    private val onlineOrderingService: OnlineOrderingService
) {

    @GetMapping("/orders")
    fun getOrders() = onlineOrderingService.getOrders()

    @PostMapping("/orders/add")
    fun addOrders(@RequestBody request: RequestOrder) =
        onlineOrderingService.addOrders(request)
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
    val username: String,
    val restaurant: String,
    val items: List<RequestItem>,
    val timeOrdered: String?
)
