package cn.wolfmc.minecraft.wolfhunter.application.config

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import org.bukkit.configuration.file.FileConfiguration

object Config {
    // TODO
    private val config: FileConfiguration
        get() = Contexts.plugin.config

    val minPlayers: Int
        get() = config.getInt("game.minPlayers", 4)

    val maxPlayers: Int
        get() = config.getInt("game.maxPlayers", 16)

    val gameStartDelay: Long
        get() = config.getLong("game.startDelay", 10)

    val borderSize: Double
        get() = config.getDouble("game.borderSize", 500.0)
}
