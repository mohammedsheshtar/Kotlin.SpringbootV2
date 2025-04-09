package com.coded.spring.ordering

import jakarta.inject.Named
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Named
interface OrderRepository: JpaRepository<OnlineOrder, Long>

@Entity
@Table(name = "orders")
data class OnlineOrder(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    // included the line below because 'user' is a reserved keyword in SQL and threw an error because of it, column escapes it
    @Column(name = "`user`")
    var user: String,

    var restaurant: String,

    //included the line below to indicate so that we can keep the items as a CSV in our database
    @Column(columnDefinition = "TEXT")
    var items: String,

    @CreationTimestamp
    var timeOrdered: LocalDateTime? = null

){
    constructor() : this(null, "", "","", null)
}