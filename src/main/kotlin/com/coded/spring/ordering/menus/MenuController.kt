package com.coded.spring.ordering.menus

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@Tag(name="MenuAPI")
@RestController
class MenuController(
    private val menuService: MenuService
) {
    @GetMapping("/menus")
    fun listMenu() = menuService.getMenu()

    @PostMapping("/menus")
    fun createMenu(@RequestBody menu: MenuDTO): MenuDTO = menuService.addMenu(menu)
}


data class MenuDTO(
    val name: String,
    val price: BigDecimal
)
