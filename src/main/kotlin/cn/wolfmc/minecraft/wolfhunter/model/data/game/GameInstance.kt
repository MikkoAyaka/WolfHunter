package cn.wolfmc.minecraft.wolfhunter.model.data.game

import cn.wolfmc.minecraft.wolfhunter.model.data.player.GamePlayer
import cn.wolfmc.minecraft.wolfhunter.model.event.StateChanged
import org.bukkit.World
import java.util.*

object GameInstance {
    var state: GameState = GameState.ENDING
        set(value) {
            if (field == value) return
            StateChanged(this, field, value).callEvent()
            field = value
        }

    var world: World? = null
    val gamePlayers = mutableMapOf<UUID, GamePlayer>()
}

enum class GameState {
    WAITING,
    STARTING,
    RUNNING,
    ENDING,
}
