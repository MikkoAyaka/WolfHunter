package cn.wolfmc.minecraft.wolfhunter.model.service

import cn.wolfmc.minecraft.wolfhunter.model.component.ListenerGroup
import cn.wolfmc.minecraft.wolfhunter.model.data.game.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.data.game.GameState
import cn.wolfmc.minecraft.wolfhunter.model.data.player.GamePlayer
import cn.wolfmc.minecraft.wolfhunter.model.data.team.GameTeam
import java.util.*

abstract class GameService : ScopeService {
    val listenerGroup = ListenerGroup()
    private var currentGame: GameInstance = GameInstance
    protected var currentStateService: ScopeService? = null
    protected val mechanism: MutableSet<ScopeService> = mutableSetOf()
    val gameTeams: MutableMap<UUID, GameTeam> = mutableMapOf()
    val gamePlayers: MutableMap<UUID, GamePlayer> = mutableMapOf()

    fun gameWait() {
        currentGame.state = GameState.WAITING
    }

    fun gameStart(): Result<Unit> {
        if (currentGame.state != GameState.WAITING) {
            return Result.failure(IllegalStateException("Game is not in waiting state"))
        }
        currentGame.state = GameState.STARTING
        prepare()
        currentGame.state = GameState.RUNNING
        return Result.success(Unit)
    }

    fun gameEnd() {
        currentGame.state = GameState.ENDING
        disable()
    }

    // 准备开始逻辑
    open fun prepare() {}
}
