package com.coded.spring.ordering.menus

import com.coded.spring.ordering.serverCache
import com.hazelcast.logging.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

private val logger = Logger.getLogger("menus")

@Service
class MenuService(
    private val menuRepository: MenuRepository,
    @Value("\${feature.festive.enabled}")
    private val festiveIsEnabled: Boolean
) {
    fun listMenu(): List<MenuEntity> {
        val menusCache = serverCache.getMap<String, List<MenuEntity>>("menus")
        if (menusCache["menus"]?.size == 0 || menusCache["menus"] == null) {
            logger.info("No menus found, caching new data...")
            if(festiveIsEnabled) {
                val menus = menuRepository.findAll().map { it.copy(
                    price = it.price
                    .multiply(BigDecimal("0.8"))
                    .setScale(3, RoundingMode.HALF_UP))
                }
                menusCache.put("menus", menus)
                return menus
            }
            else {
                val menus = menuRepository.findAll()
                menusCache.put("menus", menus)
                return menus
            }
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

        val menusCache = serverCache.getMap<String, List<MenuEntity>>("menus")
        menusCache.remove("menus")
        return MenuDTO(newMenu.name, newMenu.price)
    }
}