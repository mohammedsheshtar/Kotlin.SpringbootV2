package com.coded.spring.ordering.items

import com.coded.spring.ordering.orders.OrderEntity
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.inject.Named
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Named
interface ItemsRepository: JpaRepository<ItemsEntity, Long>

@Entity
@Table(name = "items")
data class ItemsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    // maps each item to its parent order using the foreign key items.order_id → orders.id
    @JoinColumn(name = "order_id")
    @JsonBackReference
    val order: OrderEntity,
    val name: String,

    val price: Double
){
    constructor() : this(null, OrderEntity(),   "", 0.0)
}