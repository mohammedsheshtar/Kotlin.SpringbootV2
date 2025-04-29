package com.coded.spring.ordering

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
	orderConfig.getMapConfig("menus").setTimeToLiveSeconds(60)
}

val orderConfig = Config("order-cache")
val serverCache: HazelcastInstance = Hazelcast.newHazelcastInstance(orderConfig)