package com.coded.spring.ordering.menus

import com.coded.spring.ordering.serverCache
import com.hazelcast.logging.Logger
import org.springframework.stereotype.Service
private val logger = Logger.getLogger("menus")

@Service
class MenuService(
    private val menuRepository: MenuRepository
) {
    fun getMenu(): List<MenuEntity> {
        val menusCache = serverCache.getMap<String, List<MenuEntity>>("menus")
        if (menusCache["menus"]?.size == 0 || menusCache["menus"] == null) {
            logger.info("No menus found, caching new data...")
            val menus = menuRepository.findAll()
            menusCache.put("menus", menus)
            return menus
        }
        logger.info("returning ${menusCache["menus"]?.size} menu items")
        return menusCache["menus"] ?: listOf()
    }


    fun addMenu(dto: MenuDTO): MenuDTO {
        val newMenu = MenuEntity(
            name = dto.name,
            price = dto.price
        )
        menuRepository.save(newMenu)
        // where we write invalidation for cache
        return MenuDTO(newMenu.name, newMenu.price)
    }
}