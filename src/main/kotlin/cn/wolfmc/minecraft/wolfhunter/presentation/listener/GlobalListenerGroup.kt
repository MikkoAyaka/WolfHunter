package cn.wolfmc.minecraft.wolfhunter.presentation.listener

import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.reset
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.shouldResetPlayerOnJoin
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.updateGameMode
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.updateInvulnerable
import cn.wolfmc.minecraft.wolfhunter.model.component.ListenerGroup
import cn.wolfmc.minecraft.wolfhunter.model.component.plus
import cn.wolfmc.minecraft.wolfhunter.model.component.plusAssign
import cn.wolfmc.minecraft.wolfhunter.model.event.StateChanged
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * 全局监听器组
 */
object GlobalListenerGroup {
    private val listenerGroup = ListenerGroup()

    init {
        listenerGroup.apply {
            // 游戏模式管理
            this += subscribe<PlayerJoinEvent> { it.player.updateGameMode() }
            this += subscribe<StateChanged> { onlinePlayers().forEach { it.updateGameMode() } }
            // 开局保护
            this += subscribe<PlayerJoinEvent> { it.player.updateInvulnerable() }
            this += subscribe<StateChanged> { onlinePlayers().forEach { it.updateInvulnerable() } }
            // 背包更新
            this += subscribe<PlayerJoinEvent> { if (shouldResetPlayerOnJoin(it.player)) it.player.reset() }
        }
    }

    @Awake(LifeCycle.ENABLE)
    private fun enable() {
        listenerGroup.registerAll()
    }

    @Awake(LifeCycle.DISABLE)
    private fun disable() {
        listenerGroup.unregisterAll()
    }
}
