package cn.wolfmc.minecraft.wolfhunter.model.data

import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.defaultScoreboardTeam
import cn.wolfmc.minecraft.wolfhunter.model.component.VirtualRepository
import java.util.*

class GameTeam(
    val name: String,
    private val members: MutableSet<GamePlayer> = mutableSetOf(),
) {
    val uuid: UUID = UUID.randomUUID()
    val virtualRepository = VirtualRepository()
    val scoreboardTeam = defaultScoreboardTeam(name)

    fun size() = members.size

    fun get(name: String): GamePlayer? {
        for (offlinePlayer in members) {
            if (offlinePlayer.name == null) continue
            if (offlinePlayer.name.equals(name, ignoreCase = true)) return offlinePlayer
        }
        return null
    }

    fun get(): List<GamePlayer> = members.toList()

    fun join(player: GamePlayer) {
        members.add(player)
        scoreboardTeam.addEntry(player.name!!)
    }

    fun leave(player: GamePlayer) {
        members.remove(player)
        scoreboardTeam.removeEntry(player.name!!)
    }

    fun contains(player: GamePlayer): Boolean = members.contains(player)
}
