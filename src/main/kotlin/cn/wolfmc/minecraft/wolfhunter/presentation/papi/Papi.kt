package cn.wolfmc.minecraft.wolfhunter.presentation.papi

import cn.wolfmc.minecraft.wolfhunter.common.extensions.gamePlayer
import cn.wolfmc.minecraft.wolfhunter.common.extensions.targetPointer
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object Papi : PlaceholderExpansion {
    override val identifier: String = "wolfhunter"

    override fun onPlaceholderRequest(
        player: Player?,
        args: String,
    ): String =
        when (args) {
            // 队伍总数
            "teams" -> "${GameInstance.allGameTeams().size}"
            // 玩家队伍名称
            "team_name" -> player?.gamePlayer()?.team?.name ?: "<yellow>暂无</yellow>"
            // 玩家队伍剩余人数
            "team_players" -> "${player?.gamePlayer()?.team?.size() ?: 0}"
            // 世界中心指示器
            "center_pointer" -> "${player?.targetPointer(player.world.worldBorder.center)}"
            else -> "Unknown Papi: $args"
        }
}
