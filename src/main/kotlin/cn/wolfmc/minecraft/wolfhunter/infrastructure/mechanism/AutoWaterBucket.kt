package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.playerHandheldInventory
import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import cn.wolfmc.minecraft.wolfhunter.common.extensions.wait
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import taboolib.expansion.chain
import kotlin.random.Random

object AutoWaterBucket : ScopeService {
    override fun init() {
    }

    private const val CHANCE = 0.2
    private const val COOLDOWN = 20 * 5
    private var listener: Listener? = null

    override fun enable() {
        listener =
            subscribe<EntityDamageEvent> {
                if (it.entityType != EntityType.PLAYER) return@subscribe
                when (it.cause) {
                    EntityDamageEvent.DamageCause.FALL, EntityDamageEvent.DamageCause.LAVA -> {
                        decision(it)
                    }
                    else -> return@subscribe
                }
            }
    }

    override fun disable() {
        listener?.unregister()
    }

    private fun decision(event: EntityDamageEvent) {
        val player = event.entity as Player
        // 在 CD
        if (player.hasCooldown(Material.WATER_BUCKET)) return
        // 整个背包都没水桶
        if (!player.inventory.contains(Material.WATER_BUCKET)) return
        // 玩家手持位有水桶
        if (player.inventory
                .playerHandheldInventory()
                .map { it.type }
                .contains(Material.WATER_BUCKET)
        ) {
            return
        }
        // 背包有水桶但没有放在手持位
        player.setCooldown(Material.WATER_BUCKET, COOLDOWN)
        if (Random.nextDouble() <= CHANCE) {
            event.isCancelled = true
            val recycle = event.cause != EntityDamageEvent.DamageCause.LAVA
            useWaterBucket(player, recycle)
        }
    }

    private fun useWaterBucket(
        player: Player,
        recycle: Boolean,
    ) {
        val block = player.location.block
        // 放不了水
        if (!block.isEmpty && !block.isPassable) return
        val loc = player.location.clone()
        loc.block.type = Material.WATER
        if (recycle) {
            chain {
                wait(4)
                loc.block.type = Material.AIR
            }
        } else {
            player.inventory.storageContents
                .mapIndexed { index, itemStack ->
                    index to itemStack
                }.first { it.second?.type == Material.WATER_BUCKET }
                .also {
                    player.inventory.setItem(it.first, ItemStack(Material.BUCKET))
                }
        }
    }
}
