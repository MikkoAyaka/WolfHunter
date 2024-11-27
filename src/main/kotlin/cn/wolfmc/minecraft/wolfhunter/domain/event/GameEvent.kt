package cn.wolfmc.minecraft.wolfhunter.domain.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import cn.wolfmc.minecraft.wolfhunter.domain.model.game.GameInstance
import cn.wolfmc.minecraft.wolfhunter.domain.model.game.GameState

sealed class GameEvent : Event() {
    data class StateChanged(
        val game: GameInstance,
        val from: GameState,
        val to: GameState
    ) : GameEvent() {
        companion object {
            private val HANDLERS = HandlerList()
            @JvmStatic
            fun getHandlerList() = HANDLERS
        }
        override fun getHandlers() = HANDLERS
    }
} 