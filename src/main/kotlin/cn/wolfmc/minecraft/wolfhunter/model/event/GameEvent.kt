package cn.wolfmc.minecraft.wolfhunter.model.event

import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.component.GameState
import cn.wolfmc.minecraft.wolfhunter.model.data.GamePlayer
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import taboolib.platform.type.BukkitProxyEvent

sealed class GameEvent : BukkitProxyEvent() {
    data class CountdownFinished(
        val counter: ScopeService,
    ) : GameEvent()

    data class StateChanged(
        val game: GameInstance,
        val from: GameState,
        val to: GameState,
    ) : GameEvent()

    data class GamePlayerOut(
        val gamePlayer: GamePlayer,
    ) : GameEvent()
}
