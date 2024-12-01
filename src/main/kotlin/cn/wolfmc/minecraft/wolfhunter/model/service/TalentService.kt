package cn.wolfmc.minecraft.wolfhunter.model.service

import cn.wolfmc.minecraft.wolfhunter.model.data.talent.Talent
import cn.wolfmc.minecraft.wolfhunter.model.data.talent.TalentType
import org.bukkit.entity.Player

interface TalentService {
    fun activateTalent(
        player: Player,
        talentType: TalentType,
    ): Result<Unit>

    fun deactivateTalent(
        player: Player,
        talentType: TalentType,
    ): Result<Unit>

    fun getActiveTalents(player: Player): List<Talent>
}
