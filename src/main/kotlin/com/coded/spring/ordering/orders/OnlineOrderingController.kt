package com.coded.spring.ordering.orders

import com.coded.spring.ordering.items.ItemsEntity
import com.coded.spring.ordering.items.ItemsRepository
import org.springframework.web.bind.annotation.*

@RestController
class OnlineOrderingController(
    val orderRepository: OrderRepository,
    val itemsRepository: ItemsRepository
) {

    @GetMapping("/orders")
    fun getOrders() = orderRepository.findAll().sortedBy { it.timeOrdered }

    @PostMapping("/orders")
    fun addOrders(@RequestBody request: RequestOrder): OrderEntity {
        //adding the new order into our database
        val order = orderRepository.save(
            OrderEntity(
                user = request.user,
                restaurant = request.restaurant
            )
        )

        //converting each item in our items objects list into an item entity to add them into the items database while also connecting each item to its order
        val items = request.items.map { item ->
            ItemsEntity(
                order = order,
                name = item.name,
                price = item.price
            )
        }
        itemsRepository.saveAll(items)

        return order
    }

}


// the DTO (Data Transfer Object) for our orders and items list
data class RequestItem(
    val name: String,
    val price: Double
)

data class RequestOrder(
    val user: String,
    val restaurant: String,
    val items: List<RequestItem>
)
