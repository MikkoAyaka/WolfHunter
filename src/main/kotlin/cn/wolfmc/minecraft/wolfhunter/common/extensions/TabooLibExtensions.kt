package cn.wolfmc.minecraft.wolfhunter.common.extensions

import kotlinx.coroutines.withContext
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.expansion.AsyncDispatcher
import taboolib.expansion.Chain
import taboolib.expansion.DurationType
import taboolib.module.ui.ClickEvent
import taboolib.module.ui.ClickType
import java.util.concurrent.CompletableFuture

suspend fun Chain<*>.wait(tick: Long) = wait(tick, DurationType.MINECRAFT_TICK)

// 命名 async 会冲突，无法使用，只能用 launch
suspend fun <T> Chain<*>.launch(block: suspend () -> T) = withContext(AsyncDispatcher) { block() }

typealias TBJob = CompletableFuture<*>

fun TBJob.cancel() = cancel(true)

fun ClickEvent.onLClick(consumer: InventoryClickEvent.(player: HumanEntity) -> Unit): ClickEvent {
    if (clickType == ClickType.CLICK) {
        val event = clickEvent()
        if (event.isLeftClick && !event.isShiftClick) consumer(event, event.whoClicked)
    }
    return this
}

fun ClickEvent.onSLClick(consumer: InventoryClickEvent.(player: HumanEntity) -> Unit): ClickEvent {
    if (clickType == ClickType.CLICK) {
        val event = clickEvent()
        if (event.isLeftClick && event.isShiftClick) consumer(event, event.whoClicked)
    }
    return this
}

fun ClickEvent.onRClick(consumer: InventoryClickEvent.(player: HumanEntity) -> Unit): ClickEvent {
    if (clickType == ClickType.CLICK) {
        val event = clickEvent()
        if (event.isLeftClick && !event.isShiftClick) consumer(event, event.whoClicked)
    }
    return this
}

fun ClickEvent.onSRClick(consumer: InventoryClickEvent.(player: HumanEntity) -> Unit): ClickEvent {
    if (clickType == ClickType.CLICK) {
        val event = clickEvent()
        if (event.isLeftClick && event.isShiftClick) consumer(event, event.whoClicked)
    }
    return this
}
