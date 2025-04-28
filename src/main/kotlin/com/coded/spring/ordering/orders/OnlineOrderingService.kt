package com.coded.spring.ordering.orders

import com.coded.spring.ordering.items.ItemsEntity
import com.coded.spring.ordering.items.ItemsRepository
import com.coded.spring.ordering.users.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class OnlineOrderingService(
    private val orderRepository: OrderRepository,
    private val itemsRepository: ItemsRepository,
    private var userRepository: UserRepository
) {
    fun getOrders(): List<OrderEntity> = orderRepository.findAll().filter { it.user != null }.sortedBy { it.timeOrdered }

    fun addOrders(request: RequestOrder): ResponseEntity<Any> {
        val user = userRepository.findById(request.userId).orElse(null)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "user with ID ${request.userId} was not found"))

        if (request.items.any { it.price < 0.0 }) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("error" to "item price cannot be negative"))
        }

        val order = orderRepository.save(
            OrderEntity(
                user = user,
                restaurant = request.restaurant
            )
        )

        val items = request.items.map { item ->
            ItemsEntity(
                order = order,
                name = item.name,
                price = item.price
            )
        }

        itemsRepository.saveAll(items)

        return ResponseEntity.status(HttpStatus.OK).body(
            OrderResponseDTO(
                orderId = order.id!!,
                username = user.username,
                restaurant = order.restaurant,
                timeOrdered = order.timeOrdered.toString(),
                items = items.map {
                    RequestItem(name = it.name, price = it.price)
                }
            )
        )
    }
}
