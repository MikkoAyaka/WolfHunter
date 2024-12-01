package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.*
import cn.wolfmc.minecraft.wolfhunter.model.component.plusAssign
import cn.wolfmc.minecraft.wolfhunter.model.data.game.GameState
import cn.wolfmc.minecraft.wolfhunter.model.event.StateChanged
import cn.wolfmc.minecraft.wolfhunter.model.service.GameService

object UHCGameService : GameService() {
    override fun init() {
        listOf(UHCWaitingStage, UHCStartingStage, UHCRunningStage, UHCEndingStage).forEach {
            it.init()
        }
        mechanism.addAll(
            mutableSetOf(
                BowAiming,
                FastFurnace,
                ForeverNightVision,
                RangeMining,
                AutoWaterBucket,
                KnockBackFishingRod,
                Scaffold,
                ItemSpawnHandler,
            ),
        )
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
