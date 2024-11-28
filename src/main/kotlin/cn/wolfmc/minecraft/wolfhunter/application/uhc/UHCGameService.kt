package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.domain.component.plusAssign
import cn.wolfmc.minecraft.wolfhunter.domain.event.StateChanged
import cn.wolfmc.minecraft.wolfhunter.domain.model.game.GameState
import cn.wolfmc.minecraft.wolfhunter.domain.service.GameService
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.BowAiming
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.FastFurnace
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.ForeverNightVision
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.RangeMining

object UHCGameService : GameService() {

    override fun init() {
        listOf(UHCWaitingStage, UHCStartingStage, UHCRunningStage, UHCEndingStage).forEach {
            it.init()
        }
        mechanism.addAll(mutableSetOf(BowAiming, FastFurnace, ForeverNightVision, RangeMining))
        mechanism.forEach { it.init() }

        // 阶段变更
        listenerGroup +=
            subscribe<StateChanged> { e ->
                currentStateService?.disable()
                currentStateService =
                    when (e.to) {
                        GameState.WAITING -> UHCWaitingStage
                        GameState.STARTING -> UHCStartingStage
                        GameState.RUNNING -> UHCRunningStage
                        GameState.ENDING -> UHCEndingStage
                    }
                currentStateService?.enable()
            }
    }

    override fun enable() {
        listenerGroup.registerAll()
        mechanism.forEach { it.enable() }
        gameWait()
    }

    override fun disable() {
        listenerGroup.unregisterAll()
        mechanism.forEach { it.disable() }
    }
}
