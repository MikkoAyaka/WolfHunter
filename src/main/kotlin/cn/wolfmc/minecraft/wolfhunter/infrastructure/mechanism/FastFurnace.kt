package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.domain.component.ListenerGroup
import cn.wolfmc.minecraft.wolfhunter.domain.component.plusAssign
import cn.wolfmc.minecraft.wolfhunter.domain.service.ScopeService
import org.bukkit.event.inventory.FurnaceBurnEvent
import org.bukkit.event.inventory.FurnaceStartSmeltEvent

object FastFurnace : ScopeService {
    private var listenerGroup = ListenerGroup()
    private var speedMultiple = 4.0

    override fun init() {}

    override fun enable() {
        listenerGroup +=
            subscribe<FurnaceBurnEvent> {
                val originalBurnTime = it.burnTime
                it.burnTime = (originalBurnTime / speedMultiple).toInt()
            }
        listenerGroup +=
            subscribe<FurnaceStartSmeltEvent> {
                it.totalCookTime = (it.totalCookTime / speedMultiple).toInt()
            }
    }

    override fun disable() {
        listenerGroup.unregisterAll()
        listenerGroup.clear()
    }
}
