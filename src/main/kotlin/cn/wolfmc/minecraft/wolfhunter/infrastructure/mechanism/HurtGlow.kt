package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffectType

object HurtGlow : ScopeService {
    override fun init() {
    }

    private var listener: Listener? = null

    override fun enable() {
        listener =
            subscribe(EntityDamageEvent::class) {
                val player = it.entity as? Player ?: return@subscribe
                player.addPotionEffect(
                    PotionEffectType.GLOWING
                        .createEffect(10, 0)
                        .withIcon(false)
                        .withParticles(false),
                )
            }
    }

    override fun disable() {
        listener?.unregister()
    }
}
