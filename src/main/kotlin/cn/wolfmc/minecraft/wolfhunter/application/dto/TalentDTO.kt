package cn.wolfmc.minecraft.wolfhunter.application.dto

import cn.wolfmc.minecraft.wolfhunter.domain.model.talent.TalentType

data class TalentDTO(
    val type: TalentType,
    val cooldown: Long,
    val description: String
) 