package cn.wolfmc.minecraft.wolfhunter

import cn.wolfmc.minecraft.wolfhunter.application.config.WolfHunterConfig
import cn.wolfmc.minecraft.wolfhunter.application.service.GameService
import cn.wolfmc.minecraft.wolfhunter.presentation.command.WolfHunterCommand
import org.bukkit.plugin.java.JavaPlugin

class WolfHunterPlugin : JavaPlugin() {
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
        
        logger.info("狼猎人插件已启用")
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
        logger.info("狼猎人插件已禁用")
    }
} 