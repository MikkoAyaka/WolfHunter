package cn.wolfmc.minecraft.wolfhunter

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.application.config.Config
import cn.wolfmc.minecraft.wolfhunter.application.service.UHCGameService
import cn.wolfmc.minecraft.wolfhunter.common.extensions.logT
import cn.wolfmc.minecraft.wolfhunter.presentation.command.registerCommands
import cn.wolfmc.minecraft.wolfhunter.presentation.i18n.I18n
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

object WolfHunterPlugin : JavaPlugin() {

    override fun onEnable() {
        Contexts.logger = logger
        // 初始化配置
        Config.init(this)
        Config.load()

        I18n.loadLanguage("zh")
        
        // 初始化服务(通过配置文件调整模式)
        Contexts.gameService = UHCGameService
        Contexts.gameService.init()
        Contexts.gameService.enable()
        
        // 注册命令
        registerCommands(this)

        logger.logT(Level.INFO,"plugin.enable")
    }
    
    override fun onDisable() {
        Contexts.gameService.disable()
        logger.logT(Level.INFO,"plugin.disable")
    }
} 