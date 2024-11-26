package cn.wolfmc.minecraft.wolfhunter.application.dto

import java.util.UUID

data class PlayerDTO(
    val id: UUID,
    val name: String,
    val talents: List<TalentDTO>,
    val stats: PlayerStatsDTO
)

data class PlayerStatsDTO(
    val experience: Int,
    val level: Int,
    val skillPoints: Int
) 