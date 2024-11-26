package cn.wolfmc.minecraft.wolfhunter.domain.repository

import cn.wolfmc.minecraft.wolfhunter.domain.model.player.PlayerKit
import org.bukkit.entity.Player

interface PlayerKitRepository {
    fun save(playerKit: PlayerKit)
    fun findByPlayer(player: Player): PlayerKit?
    fun delete(player: Player)
} 