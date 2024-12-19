package cn.wolfmc.minecraft.wolfhunter.application

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.application.config.Config
import cn.wolfmc.minecraft.wolfhunter.application.uhc.UHCGameService
import cn.wolfmc.minecraft.wolfhunter.common.constants.InitializeType
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.ui.SidebarHandler
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * 游戏主流程管理服务
 */
object AppService : ScopeService {
    @Awake(LifeCycle.ENABLE)
    override fun init() {
        SidebarHandler.init()
        when (Config.initializeType) {
            InitializeType.ONLY_UHC -> Contexts.gameService = UHCGameService
            // TODO
            else -> Contexts.gameService = UHCGameService
        }
    }

    @Awake(LifeCycle.ACTIVE)
    override fun enable() {
        SidebarHandler.enable()
        Contexts.gameService.init()
        Contexts.gameService.enable()
    }

    @Awake(LifeCycle.DISABLE)
    override fun disable() {
        SidebarHandler.disable()
        Contexts.gameService.disable()
    }
}
