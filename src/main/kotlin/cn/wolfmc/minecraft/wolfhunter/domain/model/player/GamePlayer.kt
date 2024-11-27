package cn.wolfmc.minecraft.wolfhunter.domain.model.player

import cn.wolfmc.minecraft.wolfhunter.domain.model.faction.Faction
import cn.wolfmc.minecraft.wolfhunter.domain.model.team.GameTeam
import org.bukkit.OfflinePlayer
import java.util.UUID

data class GamePlayer(
    val uuid: UUID,
    val name: String,
    val offlinePlayer: OfflinePlayer,
    ) {
    var faction: Faction? = null
    var team: GameTeam? = null
}
