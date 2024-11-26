package cn.wolfmc.minecraft.wolfhunter

import cn.wolfmc.minecraft.wolfhunter.application.config.WolfHunterConfig
import cn.wolfmc.minecraft.wolfhunter.application.service.GameService
import cn.wolfmc.minecraft.wolfhunter.common.extensions.legacy
import cn.wolfmc.minecraft.wolfhunter.common.extensions.logT
import cn.wolfmc.minecraft.wolfhunter.presentation.command.WolfHunterCommand
import cn.wolfmc.minecraft.wolfhunter.presentation.i18n.I18n
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

object WolfHunterPlugin : JavaPlugin() {
    private lateinit var config: WolfHunterConfig
    private lateinit var gameService: GameService
    
    override fun onEnable() {
        // 初始化配置
        config = WolfHunterConfig(this)
        config.load()
        
        // 初始化服务
        initializeServices()
        
        // 注册命令
        getCommand("wolfhunter")?.let { command ->
            val executor = WolfHunterCommand(gameService)
            command.setExecutor(executor)
            command.tabCompleter = executor
        }

        logger.logT(Level.INFO,"plugin.enable")
    }
    
    private fun initializeServices() {
        gameService = GameService(
            talentService = TODO(),
            skillService = TODO()
        )
        gameService.initialize()
    }
    
    override fun onDisable() {
        gameService.shutdown()
        logger.logT(Level.INFO,"plugin.disable")
    }
} 