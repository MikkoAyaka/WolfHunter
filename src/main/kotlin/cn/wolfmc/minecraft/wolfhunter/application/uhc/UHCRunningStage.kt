package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.setBorder
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.NoPortal
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.TeamSharedResource
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.Difficulty

object UHCRunningStage : ScopeService {
    private val stageMechanism = listOf(TeamSharedResource, NoPortal)

    override fun init() {
        stageMechanism.forEach { it.init() }
    }

    override fun enable() {
        Contexts.apply {
            worldMain.worldBorder.setSize(500.0, 10)
            worldMain.difficulty = Difficulty.HARD
            worldNether.setBorder(1.0)
            worldTheEnd.setBorder(1.0)
        }
        stageMechanism.forEach { it.enable() }
    }

    override fun disable() {
        stageMechanism.forEach { it.disable() }
    }
}
