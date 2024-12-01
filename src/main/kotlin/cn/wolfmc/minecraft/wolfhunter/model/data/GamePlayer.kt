package cn.wolfmc.minecraft.wolfhunter.model.data

import cn.wolfmc.minecraft.wolfhunter.model.data.faction.Faction
import cn.wolfmc.minecraft.wolfhunter.model.data.team.GameTeam
import org.bukkit.OfflinePlayer

/**
 * GamePlayer 代表一个处于游戏中的玩家
 * 只在整个游戏流程中短暂存在一个阶段（即游戏运行时）
 * 关联关系和构造方式都交给 GameInstance 维护
 */
class GamePlayer(
    val offlinePlayer: OfflinePlayer,
    val team: GameTeam,
    var faction: Faction? = null,
) : OfflinePlayer by offlinePlayer
