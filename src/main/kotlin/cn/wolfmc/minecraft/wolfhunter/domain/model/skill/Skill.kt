package cn.wolfmc.minecraft.wolfhunter.domain.model.skill

import cn.wolfmc.minecraft.wolfhunter.domain.model.base.Entity
import java.util.UUID

abstract class Skill : Entity<UUID> {
    abstract val type: SkillType
    abstract val duration: Long
    abstract val manaCost: Int
} 