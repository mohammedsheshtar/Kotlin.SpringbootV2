package com.coded.spring.ordering.orders

import com.coded.spring.ordering.items.ItemsEntity
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.inject.Named
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Named
interface OrderRepository: JpaRepository<OrderEntity, Long> {
    fun findByUserId(userId: Long): List<OrderEntity>
}

@Entity
@Table(name = "orders")
data class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var userId: Long? = null,

    var restaurant: String,

    // Binds each order to its related children items using the primary key orders.id → items.order_id
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @JsonManagedReference
    val items: List<ItemsEntity>? = null,



    @CreationTimestamp
    var timeOrdered: LocalDateTime? = null

){
    constructor() : this(null, null, "", null, null)
}