package cn.wolfmc.minecraft.wolfhunter.model.data.talent

abstract class Talent {
    abstract val type: TalentType
    abstract val cooldown: Long
}
