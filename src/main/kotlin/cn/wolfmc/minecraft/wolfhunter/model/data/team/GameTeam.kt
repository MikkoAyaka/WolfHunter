package cn.wolfmc.minecraft.wolfhunter.model.data.team

import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.defaultScoreboardTeam
import cn.wolfmc.minecraft.wolfhunter.model.component.VirtualRepository
import org.bukkit.OfflinePlayer
import java.util.*

class GameTeam(
    val name: String,
    private val members: MutableSet<OfflinePlayer> = mutableSetOf(),
) {
    val uuid: UUID = UUID.randomUUID()
    val virtualRepository = VirtualRepository()
    val scoreboardTeam = defaultScoreboardTeam(uuid.toString())

    fun size() = members.size

    fun get(name: String): OfflinePlayer? {
        for (offlinePlayer in members) {
            if (offlinePlayer.name == null) continue
            if (offlinePlayer.name.equals(name, ignoreCase = true)) return offlinePlayer
        }
        return null
    }

    fun get(): List<OfflinePlayer> = members.toList()

    fun join(player: OfflinePlayer) {
        members.add(player)
        scoreboardTeam.addPlayer(player)
    }

    fun leave(player: OfflinePlayer) {
        members.remove(player)
        scoreboardTeam.removePlayer(player)
    }

    fun contains(player: OfflinePlayer): Boolean = members.contains(player)
}
