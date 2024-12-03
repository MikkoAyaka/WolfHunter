package cn.wolfmc.minecraft.wolfhunter.infrastructure.game

import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import cn.wolfmc.minecraft.wolfhunter.model.component.EventHandlerSet
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.data.GamePlayer
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.sound.Sounds
import org.bukkit.GameMode
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.expansion.chain

/**
 * UHC 裁判
 * 宣布玩家出局、宣布游戏结果、离线玩家清理...
 */
object UHCGameJudge : ScopeService {
    private val eventHandlerSet = EventHandlerSet()

    override fun init() {
        eventHandlerSet +=
            EventHandler(PlayerDeathEvent::class) { e ->
                val gamePlayer = GameInstance.findGamePlayer(e.player) ?: return@EventHandler
                e.isCancelled = true
                playerOut(gamePlayer)
                val world = e.player.world
                e.player.inventory.forEach {
                    world.dropItemNaturally(e.player.location, it)
                }
                e.player.inventory.clear()
                gamePlayer.player?.gameMode = GameMode.SPECTATOR
            }
        eventHandlerSet +=
            EventHandler(GameEvent.GamePlayerOut::class) {
                // 只剩下一支队伍，游戏结束
                if (GameInstance.allGameTeams().filter { it.size() > 0 }.size <= 1) GameInstance.nextState()
            }
        // 玩家离线挂起延时任务，指定时间不返场则判定淘汰
        eventHandlerSet +=
            EventHandler(PlayerQuitEvent::class) {
                val gamePlayer = GameInstance.findGamePlayer(it.player) ?: return@EventHandler
                quitPlayerTasks.putIfAbsent(
                    gamePlayer,
                    chain {
                        kotlinx.coroutines.delay(10000)
                        playerOut(gamePlayer)
                    },
                )
            }
        // 重新加入则取消淘汰任务
        eventHandlerSet +=
            EventHandler(PlayerJoinEvent::class) {
                val gamePlayer = GameInstance.findGamePlayer(it.player) ?: return@EventHandler
                quitPlayerTasks[gamePlayer]?.cancel()
                quitPlayerTasks.remove(gamePlayer)
            }
    }

    private val quitPlayerTasks = mutableMapOf<GamePlayer, TBJob>()

    // 淘汰玩家，并抛出事件
    private fun playerOut(gamePlayer: GamePlayer) {
        GameInstance.leave(gamePlayer)
        onlinePlayers().forEach {
            it.playSound(Sounds.THUNDER)
            it.sendMessage("<red>玩家 <white>${gamePlayer.name}</white> 从本场游戏中淘汰了！</red>".miniMsg())
        }
        GameEvent.GamePlayerOut(gamePlayer).callEvent()
    }

    override fun enable() {
        eventHandlerSet.registerAll()
    }

    override fun disable() {
        eventHandlerSet.unregisterAll()
    }
}
