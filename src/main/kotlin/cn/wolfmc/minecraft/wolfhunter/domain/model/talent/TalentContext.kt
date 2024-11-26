package cn.wolfmc.minecraft.wolfhunter.domain.model.talent

import org.bukkit.entity.Player

data class TalentContext(
    val player: Player,
    val level: Int,
    val activationTime: Long
) 