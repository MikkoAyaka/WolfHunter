package cn.wolfmc.minecraft.wolfhunter.application.api

import cn.wolfmc.minecraft.wolfhunter.domain.service.GameService
import taboolib.platform.BukkitPlugin

object Contexts {
    val plugin by lazy { BukkitPlugin.getInstance() }
    lateinit var gameService: GameService
    val logger by lazy { plugin.logger }
}
