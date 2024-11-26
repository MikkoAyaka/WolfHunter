package cn.wolfmc.minecraft.wolfhunter.domain.repository

import cn.wolfmc.minecraft.wolfhunter.domain.model.talent.Talent
import cn.wolfmc.minecraft.wolfhunter.domain.model.talent.TalentType
import cn.wolfmc.minecraft.wolfhunter.domain.repository.base.Repository
import java.util.UUID

interface TalentRepository : Repository<Talent, UUID> {
    fun findByType(type: TalentType): List<Talent>
} 