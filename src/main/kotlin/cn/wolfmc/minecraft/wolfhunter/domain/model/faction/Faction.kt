package cn.wolfmc.minecraft.wolfhunter.domain.model.faction

enum class Force {
    Human,
    Spirit,
    Ender,
}

enum class Wings {
    Aggressive,
    Neutral,
    Defensive,
}

sealed class Faction(force: Force, wings: Wings) {

    data object Shadowblade : Faction(Force.Human, Wings.Aggressive)

    data object VoidsJudgement : Faction(Force.Human, Wings.Neutral)

    data object TwilightAccord : Faction(Force.Human, Wings.Defensive)

    data object Moonfang : Faction(Force.Spirit, Wings.Aggressive)

    data object StarlightCrown : Faction(Force.Spirit, Wings.Neutral)

    data object AetherBastion : Faction(Force.Spirit, Wings.Defensive)
}
