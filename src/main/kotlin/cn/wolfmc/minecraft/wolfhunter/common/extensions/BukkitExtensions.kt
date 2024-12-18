package cn.wolfmc.minecraft.wolfhunter.common.extensions

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts.plugin
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.acos
import kotlin.math.round
import kotlin.math.sqrt
import kotlin.reflect.KClass

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

fun Listener.unregister() {
    HandlerList.unregisterAll(this)
}

class EventHandler<T : Event>(
    private val eventClass: KClass<T>,
    private val priority: EventPriority = EventPriority.NORMAL,
    private val block: Listener.(T) -> Unit,
) {
    fun register(): Listener = subscribe(eventClass, priority, block)
}

fun <T : Event> subscribe(
    eventClass: KClass<T>,
    priority: EventPriority = EventPriority.NORMAL,
    block: Listener.(T) -> Unit,
): Listener {
    val listener = object : Listener {}
    val executor = { _: Listener, e: Event ->
        if (eventClass.isInstance(e)) {
            @Suppress("UNCHECKED_CAST")
            block(listener, e as T)
        }
    }
    Bukkit
        .getPluginManager()
        .registerEvent(
            eventClass.java,
            listener,
            priority,
            executor,
            plugin,
            false,
        )
    return listener
}

fun onlinePlayers(): MutableCollection<out Player> = Bukkit.getOnlinePlayers()

fun survivalPlayers() = onlinePlayers().filter { it.gameMode == GameMode.SURVIVAL }

fun Player.nearbyPlayers(distance: Int) =
    onlinePlayers()
        .filter { it.world == this.world && it.location.distance(this.location) <= distance && it != this }

fun PotionEffectType.create(
    duration: Int,
    amplifier: Int,
): PotionEffect = PotionEffect(this, duration, amplifier)

// 获取玩家手持物品栏（快捷键1~9 和 副手）
fun Inventory.playerHandheldInventory() = (0..8).plus(40).mapNotNull { getItem(it) }

private val HEX_CODE = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

/**
 * 将颜色对象转为例如 AFAF00 的十六进制颜色字符
 */
fun Color.toHex(): String {
    val chars = arrayOf('0', '0', '0', '0', '0', '0')
    chars[0] = HEX_CODE[red / 16]
    chars[1] = HEX_CODE[red % 16]
    chars[2] = HEX_CODE[green / 16]
    chars[3] = HEX_CODE[green % 16]
    chars[4] = HEX_CODE[blue / 16]
    chars[5] = HEX_CODE[blue % 16]
    return chars.joinToString(separator = "")
}

infix fun Pair<Color, Color>.ofGradient(percent: Double): Color {
    val startColor = this.first
    val endColor = this.second

    val newRed = (startColor.red + (endColor.red - startColor.red) * percent).toInt()
    val newGreen = (startColor.green + (endColor.green - startColor.green) * percent).toInt()
    val newBlue = (startColor.blue + (endColor.blue - startColor.blue) * percent).toInt()

    return Color.fromRGB(newRed, newGreen, newBlue)
}

/**
 * 转换为 MiniMessage API 的颜色格式，如 <#00FF35>
 */
fun Color.toHexFormat(): String = "<#${this.toHex()}>"

/**
 * 计算玩家当前视角与目的地坐标之间的视角夹角
 */
fun Player.targetYaw(target: Location): Float {
    if (this.world != target.world) return 0f
    val direction = target.subtract(this.location).toVector().normalize()
    val cosTheta = direction.z / sqrt(direction.x * direction.x + direction.z * direction.z)
    var angle: Float = Math.toDegrees(acos(cosTheta)).toFloat()
    if (direction.x > 0) {
        angle = -angle
    }
    val playerYaw = this.location.yaw
    var deltaYaw = angle - playerYaw
    if (deltaYaw < -180) deltaYaw += 360
    if (deltaYaw > 180) deltaYaw -= 360
    return deltaYaw
}

fun Player.targetPointer(target: Location): Char {
    var deltaYaw = this.targetYaw(target)
    if (deltaYaw < 0) deltaYaw += 360
    val slices = round(deltaYaw / 45).toInt()
    val pointers = listOf('↑', '↗', '→', '↘', '↓', '↙', '←', '↖', '↑')
    return pointers[slices]
}
