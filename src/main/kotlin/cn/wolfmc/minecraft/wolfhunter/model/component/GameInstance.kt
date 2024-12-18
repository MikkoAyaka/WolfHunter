package cn.wolfmc.minecraft.wolfhunter.model.component

import cn.wolfmc.minecraft.wolfhunter.model.data.GamePlayer
import cn.wolfmc.minecraft.wolfhunter.model.data.GameTeam
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent.StateChanged
import org.bukkit.OfflinePlayer
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

    // OfflinePlayer UUID -> GamePlayer
    private val gamePlayers = mutableMapOf<UUID, GamePlayer>()

    // GameTeam UUID -> GameTeam
    private val teams = mutableMapOf<UUID, GameTeam>()

    fun allGameTeams() = teams.values.toSet()

    fun allGamePlayers() = gamePlayers.values.toSet()

    fun findGamePlayer(offlinePlayer: OfflinePlayer) = gamePlayers[offlinePlayer.uniqueId]

    fun nextState() {
        state = GameState.entries[(state.ordinal + 1) % GameState.entries.size]
    }

    fun join(
        player: OfflinePlayer,
        team: GameTeam,
    ) {
        if (findGamePlayer(player) != null) leave(gamePlayers[player.uniqueId]!!)
        val gamePlayer = GamePlayer(player, team)
        team.join(gamePlayer)
        teams.putIfAbsent(team.uuid, team)
        gamePlayers.putIfAbsent(player.uniqueId, gamePlayer)
    }

    fun leave(player: GamePlayer) {
        val team = player.team
        team.leave(player)
        if (team.size() == 0) teams.remove(team.uuid)
        gamePlayers.remove(player.uniqueId)
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
