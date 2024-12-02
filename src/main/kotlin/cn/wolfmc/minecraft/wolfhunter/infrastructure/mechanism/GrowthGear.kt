package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.constants.isArmor
import cn.wolfmc.minecraft.wolfhunter.common.constants.isAxe
import cn.wolfmc.minecraft.wolfhunter.common.constants.isPickaxe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.EventHandler
import cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler.GrowthGearHandler
import cn.wolfmc.minecraft.wolfhunter.model.component.EventHandlerSet
import cn.wolfmc.minecraft.wolfhunter.model.data.SpecialItem
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.Material
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.inventory.ItemStack

object GrowthGear : ScopeService {
    // 存储等级需求
    private val levelExpRequireList = listOf(0, 1000, 3000, 6000, 10000)
    private val eventHandlers = EventHandlerSet()

    // 存储经验
    private val expDataMap = mutableMapOf<SpecialItem, Int>()
    private val whitelistGears =
        listOf(
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS,
            Material.WOODEN_AXE,
            Material.WOODEN_HOE,
            Material.WOODEN_PICKAXE,
            Material.WOODEN_SWORD,
            Material.WOODEN_SHOVEL,
        )

    override fun init() {
        eventHandlers.apply {
            this +=
                EventHandler(CraftItemEvent::class) {
                    val craftType = it.recipe.result.type
                    // TODO 未来支持锄头和铲子
                    if (!craftType.isArmor() && !craftType.isPickaxe() && !craftType.isAxe()) return@EventHandler
                    // 特殊物品发放
                    if (craftType in whitelistGears) {
                        specializeItem(it.recipe.result)
                    } else {
                        // 禁止合成高阶装备
                        it.isCancelled = true
                    }
                }
        }
    }

    override fun enable() {
        eventHandlers.registerAll()
    }

    override fun disable() {
        eventHandlers.unregisterAll()
    }

    // 特殊化物品
    private fun specializeItem(item: ItemStack) {
        val specialItem = GrowthGearHandler.initItem(item)
        updateItem(specialItem)
    }

    fun updateItem(item: SpecialItem) {
    }
}
