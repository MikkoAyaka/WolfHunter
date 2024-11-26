package cn.wolfmc.minecraft.wolfhunter.domain.service

import cn.wolfmc.minecraft.wolfhunter.domain.model.skill.Skill
import cn.wolfmc.minecraft.wolfhunter.domain.model.skill.SkillType
import org.bukkit.entity.Player

interface SkillService {
    fun executeSkill(player: Player, skillType: SkillType): Result<Unit>
    fun cancelSkill(player: Player, skillType: SkillType): Result<Unit>
    fun getActiveSkills(player: Player): List<Skill>
} 