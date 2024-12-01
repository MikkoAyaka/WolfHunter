package cn.wolfmc.minecraft.wolfhunter.infrastructure.game

import cn.wolfmc.minecraft.wolfhunter.common.extensions.EventHandler
import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.model.component.EventHandlerSet
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.sound.Sounds
import org.bukkit.event.entity.PlayerDeathEvent

/**
 * UHC 裁判
 * 宣布玩家出局、宣布游戏结果...
 */
object UHCGameJudge : ScopeService {
    private val eventHandlerSet = EventHandlerSet()

    override fun init() {
        eventHandlerSet +=
            EventHandler(PlayerDeathEvent::class) { e ->
                val gamePlayer = GameInstance.findGamePlayer(e.player) ?: return@EventHandler
                GameInstance.leave(gamePlayer)
                onlinePlayers().forEach {
                    it.playSound(Sounds.THUNDER)
                    it.sendMessage("<red>玩家 ${e.player.name} 从本场游戏中淘汰了！")
                }
            }
        eventHandlerSet +=
            EventHandler(GameEvent.GamePlayerOut::class) {
                // 只剩下一支队伍，游戏结束
                if (GameInstance.allGameTeams().filter { it.size() > 0 }.size <= 1) GameInstance.nextState()
            }
    }

    override fun enable() {
        eventHandlerSet.registerAll()
    }

    override fun disable() {
        eventHandlerSet.unregisterAll()
    }
}
