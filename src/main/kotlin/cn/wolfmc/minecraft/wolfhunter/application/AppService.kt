package cn.wolfmc.minecraft.wolfhunter.application

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.application.uhc.UHCGameService
import cn.wolfmc.minecraft.wolfhunter.model.component.ListenerGroup
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object AppService : ScopeService {
    private val listenerGroup = ListenerGroup()

    @Awake(LifeCycle.CONST)
    override fun init() {
        // 初始化服务(通过配置文件调整模式)
        Contexts.gameService = UHCGameService
    }

    @Awake(LifeCycle.ENABLE)
    override fun enable() {
        listenerGroup.registerAll()
        Contexts.gameService.init()
        Contexts.gameService.enable()
    }

    @Awake(LifeCycle.DISABLE)
    override fun disable() {
        listenerGroup.unregisterAll()
        Contexts.gameService.disable()
    }
}
