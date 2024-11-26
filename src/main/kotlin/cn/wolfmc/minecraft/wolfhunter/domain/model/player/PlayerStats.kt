package cn.wolfmc.minecraft.wolfhunter.domain.model.player

import cn.wolfmc.minecraft.wolfhunter.domain.model.base.Entity
import java.util.UUID

data class PlayerStats(
    override val id: UUID,
    val experience: Int,
    val level: Int,
    val skillPoints: Int
) : Entity<UUID> 