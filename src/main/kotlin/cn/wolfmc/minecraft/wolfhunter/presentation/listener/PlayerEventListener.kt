package cn.wolfmc.minecraft.wolfhunter.presentation.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class PlayerEventListener(
    private val plugin: JavaPlugin
) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        // 处理玩家加入事件
    }
    
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        // 处理玩家退出事件
    }
} 