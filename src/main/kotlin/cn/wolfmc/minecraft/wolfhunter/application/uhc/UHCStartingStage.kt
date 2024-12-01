package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.AutomaticGameStarter
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.ReadyCounter
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.event.CountdownFinished
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.bossbar.gameStarterBossBar

object UHCStartingStage : ScopeService {
    private val readyCounter = ReadyCounter

    override fun init() {
        readyCounter.init()
    }

    private val bar = gameStarterBossBar(readyCounter, 15)

    override fun enable() {
        // 计时开始
        readyCounter.enable()
        // 监听计时结束
        subscribe(CountdownFinished::class) {
            if (it.counter is AutomaticGameStarter) {
                readyCounter.disable()
                GameInstance.nextState()
                unregister()
            }
        }
    }

    override fun disable() {
    }
}
