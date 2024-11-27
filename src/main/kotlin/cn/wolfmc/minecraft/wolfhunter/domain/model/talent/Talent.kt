package cn.wolfmc.minecraft.wolfhunter.domain.model.talent

abstract class Talent {
    abstract val type: TalentType
    abstract val cooldown: Long
}