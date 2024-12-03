package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.common.extensions.EventHandler
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.*
import cn.wolfmc.minecraft.wolfhunter.model.component.GameState
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent
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
                HealthBooster,
                GrowthGear,
                HurtGlow,
            ),
        )
        mechanism.forEach { it.init() }

        // 阶段变更
        eventHandlerSet +=
            EventHandler(GameEvent.StateChanged::class) { e ->
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
        eventHandlerSet.registerAll()
        mechanism.forEach { it.enable() }
        currentGame.state = GameState.WAITING
    }

    override fun disable() {
        eventHandlerSet.unregisterAll()
        mechanism.forEach { it.disable() }
    }
}
