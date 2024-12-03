package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.EventHandler
import cn.wolfmc.minecraft.wolfhunter.common.extensions.isSpecialItem
import cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler.ScaffoldBlockHandler
import cn.wolfmc.minecraft.wolfhunter.model.component.EventHandlerSet
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

object Scaffold : ScopeService {
    override fun init() {
        eventHandlerSet.apply {
            // 更新物品状态
            this +=
                EventHandler(BlockPlaceEvent::class) {
                    val item = it.itemInHand
                    val specialItem = ScaffoldBlockHandler.get(item) ?: return@EventHandler
                    ScaffoldBlockHandler.updateItem(it.player, specialItem, item)
                }
            // 防止玩家乱丢
            this +=
                EventHandler(PlayerDropItemEvent::class) {
                    val items = it.itemDrop
                    if (!items.itemStack.isSpecialItem(ScaffoldBlockHandler)) return@EventHandler
                    it.isCancelled = true
                }
            // 切换状态
            this +=
                EventHandler(PlayerInteractEvent::class) {
                    if (!it.action.isLeftClick) return@EventHandler
                    val item = it.item ?: return@EventHandler
                    val specialItem = ScaffoldBlockHandler.get(item) ?: return@EventHandler
                    ScaffoldBlockHandler.toggle(it.player, specialItem, item)
                }
            this +=
                EventHandler(ItemSpawnEvent::class) {
                    if (!it.entity.itemStack.isSpecialItem(ScaffoldBlockHandler)) return@EventHandler
                    it.entity.remove()
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
