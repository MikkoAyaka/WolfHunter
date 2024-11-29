package cn.wolfmc.minecraft.wolfhunter.common.extensions

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts.plugin
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

fun Player.giveItemSafely(item: ItemStack) {
    val leftover = inventory.addItem(item)
    if (leftover.isNotEmpty()) {
        world.dropItem(location, item)
    }
}

fun Runnable.runTaskTimer(
    delay: Long,
    period: Long,
) {
    Bukkit.getScheduler().runTaskTimer(plugin, this, delay, period)
}

fun Runnable.runTaskTimerAsync(
    delay: Long,
    period: Long,
) {
    Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, delay, period)
}

fun Runnable.runTaskLater(delay: Long) {
    Bukkit.getScheduler().runTaskLater(plugin, this, delay)
}

fun Runnable.runTaskLaterAsync(delay: Long) {
    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, delay)
}

// 扩展函数：打开菜单
fun CommandSender.openMenu(inventory: Inventory) {
    if (this is Player) {
        this.openInventory(inventory)
    }
}

fun Listener.register() {
    Bukkit.getPluginManager().registerEvents(this, plugin)
}

fun Listener.unregister() {
    HandlerList.unregisterAll(this)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Event> subscribe(noinline block: (T) -> Unit): Listener {
    val listener = object : Listener {}
    val executor = { _: Listener, e: T ->
        // 疑似是 Bukkit 的锅，单独写监听器，只要用 registerEvent 注册，也会存在这个问题
        if (e is T) {
            block(e)
        }
    }
    Bukkit
        .getPluginManager()
        .registerEvent(
            T::class.java,
            listener,
            EventPriority.NORMAL,
            executor as (Listener, Event) -> Unit,
            plugin,
            false,
        )
    return listener
}

fun onlinePlayers() = Bukkit.getOnlinePlayers()

fun survivalPlayers() = Bukkit.getOnlinePlayers().filter { it.gameMode == GameMode.SURVIVAL }

fun Player.nearbyPlayers(distance: Int) =
    onlinePlayers()
        .filter { it.world == this.world && it.location.distance(this.location) <= distance && it != this }

fun PotionEffectType.create(
    duration: Int,
    amplifier: Int,
): PotionEffect = PotionEffect(this, duration, amplifier)

// 获取玩家手持物品栏（快捷键1~9 和 副手）
fun Inventory.playerHandheldInventory() = (0..8).plus(40).mapNotNull { getItem(it) }
