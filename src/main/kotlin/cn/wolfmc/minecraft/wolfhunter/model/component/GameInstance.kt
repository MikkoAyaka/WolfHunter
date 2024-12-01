package cn.wolfmc.minecraft.wolfhunter.model.component

import cn.wolfmc.minecraft.wolfhunter.model.data.player.GamePlayer
import cn.wolfmc.minecraft.wolfhunter.model.data.team.GameTeam
import cn.wolfmc.minecraft.wolfhunter.model.event.StateChanged
import taboolib.common.platform.function.runTask
import java.util.*

object GameInstance {
    var state: GameState = GameState.ENDING
        set(value) {
            if (field == value) return
            runTask {
                StateChanged(this, field, value).callEvent()
            }
            field = value
        }
    val gamePlayers = mutableMapOf<UUID, GamePlayer>()
    val teams = mutableMapOf<UUID, GameTeam>()

    fun nextState() {
        state = GameState.entries[(state.ordinal + 1) % GameState.entries.size]
    }
}

/**
 * 顺序严格控制
 */
enum class GameState {
    WAITING,
    STARTING,
    RUNNING,
    ENDING,
}
