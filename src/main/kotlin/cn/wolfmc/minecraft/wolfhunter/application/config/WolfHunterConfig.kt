package cn.wolfmc.minecraft.wolfhunter.application.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

class WolfHunterConfig(private val plugin: JavaPlugin) {
    private val config: FileConfiguration get() = plugin.config
    
    val minPlayers: Int get() = config.getInt("game.minPlayers", 2)
    val maxPlayers: Int get() = config.getInt("game.maxPlayers", 10)
    val gameStartDelay: Long get() = config.getLong("game.startDelay", 30)
    val borderSize: Double get() = config.getDouble("game.borderSize", 1000.0)
    
    fun load() {
        plugin.saveDefaultConfig()
    }
    
    fun reload() {
        plugin.reloadConfig()
    }
} 