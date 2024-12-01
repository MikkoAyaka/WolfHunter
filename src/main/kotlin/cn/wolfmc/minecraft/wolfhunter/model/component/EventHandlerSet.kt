package cn.wolfmc.minecraft.wolfhunter.model.component

import cn.wolfmc.minecraft.wolfhunter.common.extensions.EventHandler
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import org.bukkit.event.Listener

/** 统一管理多个事件处理器 */
class EventHandlerSet {
    private val handlers: MutableSet<EventHandler<*>> = mutableSetOf()
    private val cacheListeners = mutableSetOf<Listener>()

    fun clear() = handlers.clear()

    operator fun plusAssign(another: EventHandler<*>) {
        handlers.add(another)
    }

    fun registerAll() {
        cacheListeners.addAll(handlers.map { it.register() })
    }

    fun unregisterAll() {
        cacheListeners.forEach { it.unregister() }
        cacheListeners.clear()
    }
}
