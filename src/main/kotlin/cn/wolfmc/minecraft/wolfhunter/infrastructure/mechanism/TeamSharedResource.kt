package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.gamePlayer
import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.component.GameState
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

object TeamSharedResource : ScopeService {
    override fun init() {
    }

    private var listener: Listener? = null

    override fun enable() {
        listener =
            subscribe<PlayerDropItemEvent> {
                if (GameInstance.state != GameState.RUNNING) return@subscribe
                if (!it.player.isSneaking) return@subscribe
                val virtualRepository =
                    it.player
                        .gamePlayer()
                        ?.team
                        ?.virtualRepository ?: return@subscribe
                val itemStack = it.itemDrop.itemStack
                if (virtualRepository.store(it.player, itemStack)) it.itemDrop.remove()
            }
    }

    override fun disable() {
        listener?.unregister()
    }
}
