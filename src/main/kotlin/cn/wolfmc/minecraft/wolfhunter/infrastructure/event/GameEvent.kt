package cn.wolfmc.minecraft.wolfhunter.infrastructure.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import cn.wolfmc.minecraft.wolfhunter.domain.model.game.GameInstance

sealed class GameEvent : Event() {
    data class GameStarted(
        val game: GameInstance
    ) : GameEvent() {
        companion object {
            private val HANDLERS = HandlerList()
            
            @JvmStatic
            fun getHandlerList() = HANDLERS
        }
        
        override fun getHandlers() = HANDLERS
    }
    
    data class GameEnded(
        val game: GameInstance
    ) : GameEvent() {
        companion object {
            private val HANDLERS = HandlerList()
            
            @JvmStatic
            fun getHandlerList() = HANDLERS
        }
        
        override fun getHandlers() = HANDLERS
    }
} 