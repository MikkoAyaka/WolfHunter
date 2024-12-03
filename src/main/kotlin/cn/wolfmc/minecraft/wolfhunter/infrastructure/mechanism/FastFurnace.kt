package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.EventHandler
import cn.wolfmc.minecraft.wolfhunter.model.component.EventHandlerSet
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.event.inventory.FurnaceBurnEvent
import org.bukkit.event.inventory.FurnaceStartSmeltEvent

object FastFurnace : ScopeService {
    private var eventHandlerSet = EventHandlerSet()
    private var speedMultiple = 4.0

    override fun init() {
        eventHandlerSet +=
            EventHandler(FurnaceBurnEvent::class) {
                val originalBurnTime = it.burnTime
                it.burnTime = (originalBurnTime / speedMultiple).toInt()
            }
        eventHandlerSet +=
            EventHandler(FurnaceStartSmeltEvent::class) {
                it.totalCookTime = (it.totalCookTime / speedMultiple).toInt()
            }
    }

    override fun enable() {
        eventHandlerSet.registerAll()
    }

    override fun disable() {
        eventHandlerSet.unregisterAll()
    }
}
