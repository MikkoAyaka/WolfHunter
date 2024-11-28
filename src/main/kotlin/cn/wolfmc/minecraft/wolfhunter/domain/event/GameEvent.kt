package cn.wolfmc.minecraft.wolfhunter.domain.event

import cn.wolfmc.minecraft.wolfhunter.domain.model.game.GameInstance
import cn.wolfmc.minecraft.wolfhunter.domain.model.game.GameState
import cn.wolfmc.minecraft.wolfhunter.domain.service.ScopeService
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CountdownFinished(val counter: ScopeService) : Event() {
    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    override fun getHandlers() = HANDLERS
}

class StateChanged(val game: GameInstance, val from: GameState, val to: GameState) : Event() {
    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic fun getHandlerList() = handlerList
    }
}
