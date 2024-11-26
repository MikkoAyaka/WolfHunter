package cn.wolfmc.minecraft.wolfhunter.infrastructure.event

import cn.wolfmc.minecraft.wolfhunter.domain.model.skill.Skill
import cn.wolfmc.minecraft.wolfhunter.domain.model.talent.Talent
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.entity.Player

sealed class WolfHunterEvent : Event() {
    data class TalentActivated(
        val player: Player,
        val talent: Talent
    ) : WolfHunterEvent() {
        companion object {
            private val HANDLERS = HandlerList()
            
            @JvmStatic
            fun getHandlerList() = HANDLERS
        }
        
        override fun getHandlers() = HANDLERS
    }
    
    data class SkillExecuted(
        val player: Player,
        val skill: Skill
    ) : WolfHunterEvent() {
        companion object {
            private val HANDLERS = HandlerList()
            
            @JvmStatic
            fun getHandlerList() = HANDLERS
        }
        
        override fun getHandlers() = HANDLERS
    }
} 