package cn.wolfmc.minecraft.wolfhunter.application.config

import cn.wolfmc.minecraft.wolfhunter.common.constants.InitializeType
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object Config {
    @Config
    private lateinit var config: ConfigFile

    val initializeType: InitializeType
        get() = config.getEnum("game.initializeType", InitializeType::class.java) ?: InitializeType.TOTAL

    val minPlayers: Int
        get() = config.getInt("game.minPlayers", 4)

    val maxPlayers: Int
        get() = config.getInt("game.maxPlayers", 16)

    val gameStartDelay: Long
        get() = config.getLong("game.startDelay", 10)

    val borderSize: Double
        get() = config.getDouble("game.borderSize", 500.0)
}
