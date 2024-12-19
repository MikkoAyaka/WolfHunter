package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

object NoPortal : ScopeService {
    private val blacklist =
        listOf(
            PlayerTeleportEvent.TeleportCause.NETHER_PORTAL,
            PlayerTeleportEvent.TeleportCause.END_PORTAL,
        )

    override fun init() {
    }

    private var listener: Listener? = null

    override fun enable() {
        listener =
            subscribe(PlayerTeleportEvent::class) {
                if (it.cause !in blacklist) return@subscribe
                it.isCancelled = true
            }
    }

    override fun disable() {
        listener?.unregister()
    }
}
