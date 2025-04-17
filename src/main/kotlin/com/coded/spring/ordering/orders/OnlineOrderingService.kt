package com.coded.spring.ordering.orders

import com.coded.spring.ordering.items.ItemsEntity
import com.coded.spring.ordering.items.ItemsRepository
import com.coded.spring.ordering.users.UserRepository
import org.springframework.stereotype.Service

@Service
class OnlineOrderingService(
    private val orderRepository: OrderRepository,
    private val itemsRepository: ItemsRepository,
    private var userRepository: UserRepository
) {
    fun getOrders(): List<OrderEntity> = orderRepository.findAll().filter { it.user != null }.sortedBy { it.timeOrdered }

    fun addOrders(request: RequestOrder): OrderResponseDTO {
        val user = userRepository.findById(request.userId).orElseThrow {
            IllegalArgumentException("User with ID ${request.userId} not found")
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

        return OrderResponseDTO(
            id = order.id!!,
            username = user.username,
            restaurant = order.restaurant,
            timeOrdered = order.timeOrdered.toString(),
            items = items.map {
                RequestItem(name = it.name, price = it.price)
            }
        )
    }
}
