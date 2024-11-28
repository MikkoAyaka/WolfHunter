package cn.wolfmc.minecraft.wolfhunter.domain.component

import cn.wolfmc.minecraft.wolfhunter.common.extensions.register
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import org.bukkit.event.Listener

/** 统一管理多个监听器 */
class ListenerGroup(val listeners: MutableSet<Listener>) {

    constructor(vararg listeners: Listener) : this(listeners.toMutableSet())

    fun registerAll() {
        listeners.forEach { it.register() }
    }

    fun unregisterAll() {
        listeners.forEach { it.unregister() }
    }
}

operator fun Listener.plus(another: Listener): ListenerGroup {
    return ListenerGroup(this, another)
}

operator fun ListenerGroup.plusAssign(another: Listener) {
    this.listeners += another
}
