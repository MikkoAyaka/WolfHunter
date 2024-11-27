package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.domain.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.setBorder
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.setRespawnRadius
import org.bukkit.Bukkit
import org.bukkit.Difficulty

object UHCWaitingService: ScopeService {
    override fun init() {
    }

    override fun enable() {
        Bukkit.getWorlds().forEach {
            it.setRespawnRadius(24)
            it.setBorder(50.0)
            it.difficulty = Difficulty.PEACEFUL
        }
    }

    override fun disable() {
    }
}