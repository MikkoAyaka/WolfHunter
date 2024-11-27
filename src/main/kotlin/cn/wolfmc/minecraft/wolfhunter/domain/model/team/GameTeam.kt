package cn.wolfmc.minecraft.wolfhunter.domain.model.team

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class GameTeam(
    val uuid: UUID = UUID.randomUUID(),
    private val memberUuids: MutableSet<UUID> = mutableSetOf()
): MutableSet<UUID> by memberUuids {

    fun getOfflinePlayer(name: String): OfflinePlayer? {
        for (offlinePlayer in getOfflinePlayers()) {
            if (offlinePlayer.name == null) continue
            if (offlinePlayer.name.equals(name, ignoreCase = true)) return offlinePlayer
        }
        return null
    }

    fun getPlayers(): List<Player> {
        return memberUuids
            .mapNotNull { id: UUID -> Bukkit.getPlayer(id) }
            .filter { p: Player -> p.isOnline }
            .toList()
    }

    fun getOfflinePlayers(): List<OfflinePlayer> {
        return memberUuids
            .map { id: UUID -> Bukkit.getOfflinePlayer(id) }
            .toList()
    }

    fun add(player: Player) {
        add(player.uniqueId)
    }

    fun remove(offlinePlayer: OfflinePlayer) {
        remove(offlinePlayer.uniqueId)
    }

    fun remove(player: Player) {
        remove(player.uniqueId)
    }


    fun contains(player: Player): Boolean {
        return contains(player.uniqueId)
    }
}