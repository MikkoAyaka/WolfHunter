package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.model.component.ListenerGroup
import cn.wolfmc.minecraft.wolfhunter.model.component.plusAssign
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.item.ScaffoldBlock
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

object Scaffold : ScopeService {
    override fun init() {
    }

    private var listenerGroup = ListenerGroup()

    override fun enable() {
        // 更新物品状态
        listenerGroup +=
            subscribe<BlockPlaceEvent> {
                val block = it.itemInHand
                if (!ScaffoldBlock.isSimilar(block)) return@subscribe
                ScaffoldBlock.updateItem(it.player, block)
            }
        // 防止玩家乱丢
        listenerGroup +=
            subscribe<PlayerDropItemEvent> {
                val items = it.itemDrop
                if (!ScaffoldBlock.isSimilar(items.itemStack)) return@subscribe
                it.isCancelled = true
            }
        // 切换状态
        listenerGroup +=
            subscribe<PlayerInteractEvent> {
                if (!it.action.isLeftClick) return@subscribe
                val item = it.item ?: return@subscribe
                if (!ScaffoldBlock.isSimilar(item)) return@subscribe
                ScaffoldBlock.toggle(it.player, item)
            }
    }

    override fun disable() {
        listenerGroup.unregisterAll()
    }
}
