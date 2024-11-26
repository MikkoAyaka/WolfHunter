package cn.wolfmc.minecraft.wolfhunter.domain.model.game

import org.bukkit.World
import java.util.UUID

class Game(
    val id: UUID = UUID.randomUUID(),
    var state: GameState = GameState.WAITING,
    var world: World? = null
) {
}

enum class GameState {
    WAITING,
    STARTING,
    RUNNING,
    ENDING
} 