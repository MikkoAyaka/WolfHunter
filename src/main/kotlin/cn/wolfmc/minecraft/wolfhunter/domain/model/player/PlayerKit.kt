package cn.wolfmc.minecraft.wolfhunter.domain.model.player

import cn.wolfmc.minecraft.wolfhunter.domain.model.base.Entity
import cn.wolfmc.minecraft.wolfhunter.domain.model.talent.Talent
import org.bukkit.entity.Player
import java.util.UUID

data class PlayerKit(
    override val id: UUID,
    val player: Player,
    private val talents: MutableSet<Talent> = mutableSetOf()
) : Entity<UUID> 