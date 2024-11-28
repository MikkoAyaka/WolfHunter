package cn.wolfmc.minecraft.wolfhunter.application

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.application.config.Config
import cn.wolfmc.minecraft.wolfhunter.application.uhc.UHCGameService
import cn.wolfmc.minecraft.wolfhunter.domain.component.ListenerGroup
import cn.wolfmc.minecraft.wolfhunter.domain.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.command.registerCommands
import cn.wolfmc.minecraft.wolfhunter.presentation.i18n.I18n

object AppService : ScopeService {

    private val listenerGroup = ListenerGroup()

    override fun init() {
        Contexts.logger = Contexts.plugin.logger
        // 初始化配置
        Config.init(Contexts.plugin)
        Config.load()
        I18n.initFiles()
        I18n.loadLanguage("zh")
        // 初始化服务(通过配置文件调整模式)
        Contexts.gameService = UHCGameService
    }

    override fun enable() {
        registerCommands(Contexts.plugin)
        listenerGroup.registerAll()
        Contexts.gameService.init()
        Contexts.gameService.enable()
    }

    override fun disable() {
        listenerGroup.unregisterAll()
        Contexts.gameService.disable()
    }
}
