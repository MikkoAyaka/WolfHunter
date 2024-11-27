package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.domain.component.plusAssign
import cn.wolfmc.minecraft.wolfhunter.domain.event.GameEvent
import cn.wolfmc.minecraft.wolfhunter.domain.model.game.GameState
import cn.wolfmc.minecraft.wolfhunter.domain.service.GameService
import cn.wolfmc.minecraft.wolfhunter.domain.service.ScopeService

object UHCGameService: GameService() {

    private var currentStateService: ScopeService? = null

    override fun init() {
        listOf(
            UHCWaitingService,
            UHCStartingService,
            UHCRunningService,
            UHCEndingService
        ).forEach { it.init() }

        listenerGroup += subscribe<GameEvent.StateChanged> { e ->
            currentStateService?.disable()
            currentStateService = when(e.to) {
                GameState.WAITING -> UHCWaitingService
                GameState.STARTING -> UHCStartingService
                GameState.RUNNING -> UHCRunningService
                GameState.ENDING -> UHCEndingService
            }
            currentStateService?.enable()
        }
    }

    override fun enable() {
        listenerGroup.registerAll()
    }

    override fun disable() {
        listenerGroup.unregisterAll()
    }
}