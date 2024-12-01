package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.EventHandler
import cn.wolfmc.minecraft.wolfhunter.model.component.EventHandlerSet
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.item.ScaffoldBlock
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

object Scaffold : ScopeService {
    override fun init() {
        eventHandlerSet.apply {
            // 更新物品状态
            this +=
                EventHandler(BlockPlaceEvent::class) {
                    val block = it.itemInHand
                    if (!ScaffoldBlock.isSimilar(block)) return@EventHandler
                    ScaffoldBlock.updateItem(it.player, block)
                }
            // 防止玩家乱丢
            this +=
                EventHandler(PlayerDropItemEvent::class) {
                    val items = it.itemDrop
                    if (!ScaffoldBlock.isSimilar(items.itemStack)) return@EventHandler
                    it.isCancelled = true
                }
            // 切换状态
            this +=
                EventHandler(PlayerInteractEvent::class) {
                    if (!it.action.isLeftClick) return@EventHandler
                    val item = it.item ?: return@EventHandler
                    if (!ScaffoldBlock.isSimilar(item)) return@EventHandler
                    ScaffoldBlock.toggle(it.player, item)
                }
        }
    }

    private var eventHandlerSet = EventHandlerSet()

    override fun enable() {
        eventHandlerSet.registerAll()
    }

    override fun disable() {
        eventHandlerSet.unregisterAll()
    }
}
