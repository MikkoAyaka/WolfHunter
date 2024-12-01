package cn.wolfmc.minecraft.wolfhunter.model.data.player

import cn.wolfmc.minecraft.wolfhunter.model.data.faction.Faction
import cn.wolfmc.minecraft.wolfhunter.model.data.team.GameTeam
import org.bukkit.OfflinePlayer
import java.util.*

data class GamePlayer(
    val uuid: UUID,
    val name: String,
    val offlinePlayer: OfflinePlayer,
) {
    var faction: Faction? = null
    var team: GameTeam? = null
}
