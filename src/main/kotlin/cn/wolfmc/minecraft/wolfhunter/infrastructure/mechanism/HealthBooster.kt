package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

object HealthBooster : ScopeService {
    override fun init() {
    }

    private val modifier =
        AttributeModifier(
            UUID.fromString("019385ff-007e-76b1-999c-432c930703eb"),
            "HealthBooster",
            20.0,
            AttributeModifier.Operation.ADD_NUMBER,
        )
    private var listener: Listener? = null

    override fun enable() {
        listener =
            subscribe(PlayerJoinEvent::class) { e ->
                e.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.apply {
                    if (modifiers.contains(modifier)) return@apply
                    addModifier(modifier)
                }
            }
    }

    override fun disable() {
        listener?.unregister()
    }
}
