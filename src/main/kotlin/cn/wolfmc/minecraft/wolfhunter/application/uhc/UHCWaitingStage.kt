package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.AutomaticGameStarter
import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.domain.event.GameEvent
import cn.wolfmc.minecraft.wolfhunter.domain.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.setBorder
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.setRespawnRadius
import org.bukkit.Bukkit
import org.bukkit.Difficulty

object UHCWaitingStage: ScopeService {

    private val gameStarter: ScopeService = AutomaticGameStarter

    override fun init() {
        gameStarter.init()
        subscribe<GameEvent.CountdownFinished> { if (it.counter is AutomaticGameStarter) gameStarter.disable() }
    }

    override fun enable() {
        Bukkit.getWorlds().forEach {
            it.setRespawnRadius(24)
            it.setBorder(50.0)
            it.difficulty = Difficulty.PEACEFUL
        }
        gameStarter.enable()
    }

    override fun disable() {
    }
}