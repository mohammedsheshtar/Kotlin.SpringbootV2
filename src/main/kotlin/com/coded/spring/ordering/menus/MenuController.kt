package com.coded.spring.ordering.menus

import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
class MenuController(
    private val menuService: MenuService
) {
    @GetMapping("/menus")
    fun getMenu() = menuService.getMenu()

    @PostMapping("/menus")
    fun createMenu(@RequestBody menu: MenuDTO): MenuDTO = menuService.addMenu(menu)
}


data class MenuDTO(
    val name: String,
    val price: BigDecimal
)
