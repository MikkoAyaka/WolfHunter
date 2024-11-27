package cn.wolfmc.minecraft.wolfhunter.application.service

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.application.config.Config
import cn.wolfmc.minecraft.wolfhunter.common.extensions.MenuListener
import cn.wolfmc.minecraft.wolfhunter.domain.service.ApplicationService
import cn.wolfmc.minecraft.wolfhunter.domain.component.ListenerGroup
import cn.wolfmc.minecraft.wolfhunter.presentation.command.registerCommands
import cn.wolfmc.minecraft.wolfhunter.presentation.i18n.I18n

object GlobalService: ApplicationService {

    private val listenerGroup = ListenerGroup(MenuListener)

    override fun init() {
        Contexts.logger = Contexts.plugin.logger
        // 初始化配置
        Config.init(Contexts.plugin)
        Config.load()
        I18n.initFiles()
        I18n.loadLanguage("zh")
        // 初始化服务(通过配置文件调整模式)
        Contexts.gameService = UHCGameService
        Contexts.gameService.init()
    }

    override fun enable() {
        registerCommands(Contexts.plugin)
        Contexts.gameService.enable()
        listenerGroup.registerAll()
    }

    override fun disable() {
        listenerGroup.unregisterAll()
        Contexts.gameService.disable()
    }

}