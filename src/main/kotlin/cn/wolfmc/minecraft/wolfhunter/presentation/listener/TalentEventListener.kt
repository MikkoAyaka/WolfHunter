import cn.wolfmc.minecraft.wolfhunter.infrastructure.event.WolfHunterEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class TalentEventListener(private val plugin: JavaPlugin) : Listener {
    
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }
    
    @EventHandler
    fun onTalentActivated(event: WolfHunterEvent.TalentActivated) {
        plugin.logger.info("玩家 ${event.player.name} 激活了天赋 ${event.talent}")
    }
    
    @EventHandler
    fun onSkillExecuted(event: WolfHunterEvent.SkillExecuted) {
        plugin.logger.info("玩家 ${event.player.name} 使用了技能 ${event.skill}")
    }
} 