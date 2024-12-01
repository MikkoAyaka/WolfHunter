package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.entity.Damageable
import org.bukkit.entity.EntityType
import org.bukkit.entity.FishHook
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent

object KnockBackFishingRod : ScopeService {
    override fun init() {
    }

    private var listener: Listener? = null

    override fun enable() {
        listener =
            subscribe<ProjectileHitEvent> {
                if (it.entityType == EntityType.FISHING_HOOK && it.hitEntity != null) {
                    val fishHook = it.entity as FishHook
                    val shooter = fishHook.shooter as? Player ?: return@subscribe
                    val hitEntity = it.hitEntity as? Damageable ?: return@subscribe
                    hitEntity.damage(1.0, shooter)
                }
            }
    }

    override fun disable() {
        listener?.unregister()
    }
}
