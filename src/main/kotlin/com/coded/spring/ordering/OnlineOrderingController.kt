package com.coded.spring.ordering

import org.springframework.web.bind.annotation.*

@RestController
class OnlineOrderingController(
    val orderRepository: OrderRepository
) {

    @GetMapping("/orders")
    fun getOrders() = orderRepository.findAll()

    @PostMapping("/orders")
    fun addOrders(@RequestBody request: RequestOrder): OnlineOrder {
        return orderRepository.save(OnlineOrder(
            user = request.user,
            restaurant = request.restaurant,
            items = request.items))
    }
}

data class RequestOrder(
    val user: String,
    val restaurant: String,
    val items: List<String>
)