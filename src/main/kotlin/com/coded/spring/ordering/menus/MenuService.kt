package com.coded.spring.ordering.menus

import org.springframework.stereotype.Service

@Service
class MenuService(
    private val menuRepository: MenuRepository
) {
    fun getMenu(): List<MenuEntity> = menuRepository.findAll()

    fun addMenu(dto: MenuDTO): MenuDTO {
        val newMenu = MenuEntity(
            name = dto.name,
            price = dto.price
        )
        menuRepository.save(newMenu)
        return MenuDTO(newMenu.name, newMenu.price)
    }
}
