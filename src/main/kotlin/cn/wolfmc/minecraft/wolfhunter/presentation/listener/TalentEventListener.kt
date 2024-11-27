import cn.wolfmc.minecraft.wolfhunter.domain.event.ApplicationEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class TalentEventListener(private val plugin: JavaPlugin) : Listener {
    
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }
    
    @EventHandler
    fun onTalentActivated(event: ApplicationEvent.TalentActivated) {
        plugin.logger.info("玩家 ${event.player.name} 激活了天赋 ${event.talent}")
    }

} 