package com.coded.spring.ordering.menus

import com.coded.spring.ordering.items.ItemsEntity
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.inject.Named
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Named
interface MenuRepository : JpaRepository<MenuEntity, Long>

@Entity
@Table(name = "menus")
data class MenuEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
        @Column(precision = 9, scale = 3)
        val price: BigDecimal
){
    constructor() : this(null, "", BigDecimal.ZERO)
}