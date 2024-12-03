package cn.wolfmc.minecraft.wolfhunter.presentation.listener

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.common.extensions.EventHandler
import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.reset
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.shouldResetPlayerOnJoin
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.updateGameMode
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.updateInvulnerable
import cn.wolfmc.minecraft.wolfhunter.model.component.EventHandlerSet
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent.StateChanged
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * 全局事件处理器组
 */
object GlobalEventHandlerSet {
    private val eventHandlerSet = EventHandlerSet()

    init {
        eventHandlerSet.apply {
            // 游戏模式管理
            this += EventHandler(PlayerJoinEvent::class) { it.player.updateGameMode() }
            this += EventHandler(StateChanged::class) { onlinePlayers().forEach { it.updateGameMode() } }
            // 开局保护
            this += EventHandler(PlayerJoinEvent::class) { it.player.updateInvulnerable() }
            this += EventHandler(StateChanged::class) { onlinePlayers().forEach { it.updateInvulnerable() } }
            // 背包更新
            this += EventHandler(PlayerJoinEvent::class) { if (shouldResetPlayerOnJoin(it.player)) it.player.reset() }
            // 计分板队伍
            this +=
                EventHandler(PlayerJoinEvent::class) {
                    Contexts.scoreboardTeamManager.addPlayer(it.player)
                }
        }
    }

    @Awake(LifeCycle.ENABLE)
    private fun enable() {
        eventHandlerSet.registerAll()
    }

    @Awake(LifeCycle.DISABLE)
    private fun disable() {
        eventHandlerSet.unregisterAll()
        eventHandlerSet.clear()
    }
}
