package cn.wolfmc.minecraft.wolfhunter.domain.model.game

import cn.wolfmc.minecraft.wolfhunter.domain.event.GameEvent
import cn.wolfmc.minecraft.wolfhunter.domain.model.player.GamePlayer
import org.bukkit.World
import java.util.*

object GameInstance {
    var state: GameState = GameState.WAITING
        set(value) {
            GameEvent.StateChanged(this, field, value).callEvent()
            field = value
        }
    var world: World? = null
    val gamePlayers = mutableMapOf<UUID, GamePlayer>()
}

enum class GameState {
    WAITING,
    STARTING,
    RUNNING,
    ENDING
} 