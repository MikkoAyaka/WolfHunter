package cn.wolfmc.minecraft.wolfhunter.domain.model.game

import cn.wolfmc.minecraft.wolfhunter.domain.event.StateChanged
import cn.wolfmc.minecraft.wolfhunter.domain.model.player.GamePlayer
import java.util.*
import org.bukkit.World

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
