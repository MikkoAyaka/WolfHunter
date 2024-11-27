package cn.wolfmc.minecraft.wolfhunter.domain.model.game

import org.bukkit.World

object GameInstance {
    var state: GameState = GameState.WAITING
    var world: World? = null
}

enum class GameState {
    WAITING,
    STARTING,
    RUNNING,
    ENDING
} 