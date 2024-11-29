package cn.wolfmc.minecraft.wolfhunter.domain.model.talent

enum class TalentType {
    HUNTER_SENSE,
    WOLF_INSTINCT,
    STEALTH_MASTER,
    TRACKING_EXPERT,
    ;

    companion object {
        fun fromString(value: String): TalentType? = entries.find { it.name.equals(value, ignoreCase = true) }
    }
}
