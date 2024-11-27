package cn.wolfmc.minecraft.wolfhunter.common.extensions

import cn.wolfmc.minecraft.wolfhunter.WolfHunterPlugin
import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

fun Player.giveItemSafely(item: ItemStack) {
    val leftover = inventory.addItem(item)
    if (leftover.isNotEmpty()) {
        world.dropItem(location, item)
    }
}
fun Runnable.runTaskTimer(delay: Long, period: Long) {
    Bukkit.getScheduler().runTaskTimer(Contexts.plugin, this, delay, period)
}
fun Runnable.runTaskTimerAsync(delay: Long, period: Long) {
    Bukkit.getScheduler().runTaskTimerAsynchronously(Contexts.plugin, this, delay, period)
}
fun Runnable.runTaskLater(delay: Long) {
    Bukkit.getScheduler().runTaskLater(Contexts.plugin, this, delay)
}
fun Runnable.runTaskLaterAsync(delay: Long) {
    Bukkit.getScheduler().runTaskLaterAsynchronously(Contexts.plugin, this, delay)
}

// 扩展函数：打开菜单
fun CommandSender.openMenu(inventory: Inventory) {
    if (this is Player) {
        this.openInventory(inventory)
    }
}

fun Listener.register() {
    Bukkit.getPluginManager().registerEvents(this, Contexts.plugin)
}
fun Listener.unregister() {
    HandlerList.unregisterAll(this)
}
fun <T: Event> subscribe(block: (T) -> Unit): Listener {
    return object : Listener {
        @EventHandler
        fun on(event: T) {
            block.invoke(event)
        }
    }
}
fun onlinePlayers() = Bukkit.getOnlinePlayers()