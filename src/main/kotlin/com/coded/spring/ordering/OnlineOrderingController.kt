package com.coded.spring.ordering

import org.springframework.web.bind.annotation.*

@RestController
class OnlineOrderingController(
    val orderRepository: OrderRepository
) {

    //this is the GET request, function getOrders() will be executed when this service is requested. It will display the current order list
    @GetMapping("/orders")
    fun getOrders() = orderRepository.findAll().sortedBy {it.timeOrdered}

    //this is the POST request, function addOrders() will be executed when this service is requested. It will add a new order to the list
    @PostMapping("/orders")
    fun addOrders(@RequestBody request: RequestOrder): OnlineOrder {
        return orderRepository.save(OnlineOrder(
            user = request.user,
            restaurant = request.restaurant,
            items = request.items.joinToString(", ")))
    }
}

// the DTO (Data Transfer Object) for our order list
data class RequestOrder(
    val user: String,
    val restaurant: String,
    val items: List<String>
)