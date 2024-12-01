package cn.wolfmc.minecraft.wolfhunter.model.data.team

import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.defaultScoreboardTeam
import cn.wolfmc.minecraft.wolfhunter.model.component.VirtualRepository
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class GameTeam(
    val uuid: UUID = UUID.randomUUID(),
    private val memberUuids: MutableSet<UUID> = mutableSetOf(),
) : MutableSet<UUID> by memberUuids {
    val virtualRepository = VirtualRepository()
    val scoreboardTeam = defaultScoreboardTeam(uuid.toString())

    fun getOfflinePlayer(name: String): OfflinePlayer? {
        for (offlinePlayer in getOfflinePlayers()) {
            if (offlinePlayer.name == null) continue
            if (offlinePlayer.name.equals(name, ignoreCase = true)) return offlinePlayer
        }
        return null
    }

    fun getPlayers(): List<Player> =
        memberUuids
            .mapNotNull { id: UUID -> Bukkit.getPlayer(id) }
            .filter { p: Player -> p.isOnline }
            .toList()

    fun getOfflinePlayers(): List<OfflinePlayer> = memberUuids.map { id: UUID -> Bukkit.getOfflinePlayer(id) }.toList()

    fun add(player: Player) {
        add(player.uniqueId)
    }

    fun remove(offlinePlayer: OfflinePlayer) {
        remove(offlinePlayer.uniqueId)
    }

    fun remove(player: Player) {
        remove(player.uniqueId)
    }

    fun contains(player: Player): Boolean = contains(player.uniqueId)
}
