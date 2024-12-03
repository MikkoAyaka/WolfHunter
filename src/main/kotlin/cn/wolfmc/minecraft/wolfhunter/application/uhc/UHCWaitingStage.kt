package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.AutomaticGameStarter
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.setBorder
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.setRespawnRadius
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent.CountdownFinished
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.bossbar.waitBossBar
import org.bukkit.Bukkit
import org.bukkit.Difficulty
import org.bukkit.GameRule

object UHCWaitingStage : ScopeService {
    private val gameStarter = AutomaticGameStarter

    override fun init() {
        gameStarter.init()
    }

    private val bar = waitBossBar(gameStarter, 600)

    override fun enable() {
        bar.init()
        Bukkit.getWorlds().forEach {
            it.setRespawnRadius(24)
            it.setBorder(50.0)
            it.difficulty = Difficulty.PEACEFUL
            it.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
        }
        // 计时开始
        gameStarter.enable()
        // 监听计时结束
        subscribe(CountdownFinished::class) {
            if (it.counter is AutomaticGameStarter) {
                gameStarter.disable()
                GameInstance.nextState()
                unregister()
            }
        }
    }

    override fun disable() {}
}
