package cn.wolfmc.minecraft.wolfhunter.domain.model.talent

import cn.wolfmc.minecraft.wolfhunter.domain.model.base.Entity
import java.util.UUID

abstract class Talent : Entity<UUID> {
    abstract val type: TalentType
    abstract val cooldown: Long
    abstract val description: String
} 