package cn.wolfmc.minecraft.wolfhunter.model.event

import cn.wolfmc.minecraft.wolfhunter.model.data.game.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.data.game.GameState
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CountdownFinished(
    val counter: ScopeService,
) : Event() {
    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }

    override fun getHandlers() = HANDLERS
}

class StateChanged(
    val game: GameInstance,
    val from: GameState,
    val to: GameState,
) : Event() {
    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic fun getHandlerList() = handlerList
    }
}
