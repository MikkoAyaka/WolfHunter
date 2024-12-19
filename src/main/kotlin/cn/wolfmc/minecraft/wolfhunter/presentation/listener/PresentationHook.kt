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
import cn.wolfmc.minecraft.wolfhunter.presentation.ui.showBattleStatusTitle
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submit

/**
 * 表现层钩子(触发式调用)
 */
object PresentationHook {
    private val eventHandlerSet = EventHandlerSet()

    init {
        eventHandlerSet.apply {
            // 游戏模式管理
            this += EventHandler(PlayerJoinEvent::class) { it.player.updateGameMode() }
            this += EventHandler(PlayerRespawnEvent::class) { it.player.updateGameMode() }
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
            // 战斗状态标题(近战)
            this +=
                EventHandler(EntityDamageByEntityEvent::class) {
                    if (it.entityType != EntityType.PLAYER) return@EventHandler
                    if (it.damager.type != EntityType.PLAYER) return@EventHandler
                    val attacker = it.damager as Player
                    val defender = it.entity as Player
                    submit(async = true, delay = 1) {
                        showBattleStatusTitle(attacker, defender, it.finalDamage)
                    }
                }
            // 战斗状态标题(远程)
            this +=
                EventHandler(EntityDamageByEntityEvent::class) {
                    if (it.entityType != EntityType.PLAYER) return@EventHandler
                    if (it.entity !is Projectile) return@EventHandler
                    val attacker = (it.entity as Projectile).shooter as? Player ?: return@EventHandler
                    val defender = it.entity as Player
                    submit(async = true, delay = 1) {
                        showBattleStatusTitle(attacker, defender, it.finalDamage)
                    }
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
    }
}
