package com.coded.spring.ordering.orders

import com.coded.spring.ordering.items.ItemsEntity
import com.coded.spring.ordering.items.ItemsRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class OnlineOrderingService(
    private val orderRepository: OrderRepository,
    private val itemsRepository: ItemsRepository,
) {
    fun getOrders(userId: Long): List<OrderResponseDTO> {
        //return orderRepository.findAll().filter { it.userId != null }.sortedBy { it.timeOrdered }
        return orderRepository.findByUserId(userId).filter { it.userId != null }.sortedBy { it.timeOrdered }.map {
            order -> OrderResponseDTO(
                orderId = order.id ?:
                throw IllegalStateException("Order has no id..."),
                restaurant = order.restaurant,
                items = order.items!!.map {
                    RequestItem(
                        name = it.name,
                        price = it.price
                    )
                },
            timeOrdered = order.timeOrdered.toString()
            )
        }
    }
    fun addOrders(userId: Long, request: RequestOrder): ResponseEntity<Any> {
        val user = orderRepository.findById(request.userId).orElse(null)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "user with ID ${request.userId} was not found"))

        if (request.items.any { it.price < 0.0 }) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("error" to "item price cannot be negative"))
        }

        val order = orderRepository.save(
            OrderEntity(
                userId = userId,
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
                restaurant = order.restaurant,
                timeOrdered = order.timeOrdered.toString(),
                items = items.map {
                    RequestItem(name = it.name, price = it.price)
                }
            )
        )
    }
}

