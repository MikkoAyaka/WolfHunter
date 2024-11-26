package cn.wolfmc.minecraft.wolfhunter.domain.model.game

import cn.wolfmc.minecraft.wolfhunter.domain.model.player.PlayerKit
import org.bukkit.World
import java.util.UUID

class Game(
    val id: UUID = UUID.randomUUID(),
    var state: GameState = GameState.WAITING,
    private val players: MutableMap<UUID, PlayerKit> = mutableMapOf(),
    var world: World? = null
) {
    fun addPlayer(playerKit: PlayerKit) {
        players[playerKit.player.uniqueId] = playerKit
    }
    
    fun removePlayer(playerId: UUID) {
        players.remove(playerId)
    }
    
    fun getPlayer(playerId: UUID): PlayerKit? = players[playerId]
    
    fun getAllPlayers(): List<PlayerKit> = players.values.toList()
}

enum class GameState {
    WAITING,
    STARTING,
    RUNNING,
    ENDING
} 