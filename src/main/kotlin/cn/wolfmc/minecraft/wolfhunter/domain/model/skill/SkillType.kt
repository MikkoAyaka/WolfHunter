package cn.wolfmc.minecraft.wolfhunter.domain.model.skill

enum class SkillType {
    SPRINT,
    STEALTH,
    TRACK,
    HOWL;
    
    companion object {
        fun fromString(value: String): SkillType? =
            entries.find { it.name.equals(value, ignoreCase = true) }
    }
} 