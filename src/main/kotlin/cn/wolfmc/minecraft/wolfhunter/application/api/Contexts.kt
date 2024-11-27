package cn.wolfmc.minecraft.wolfhunter.application.api

import cn.wolfmc.minecraft.wolfhunter.domain.service.GameService
import java.util.logging.Logger

object Contexts {
    lateinit var gameService: GameService
    lateinit var logger: Logger
}