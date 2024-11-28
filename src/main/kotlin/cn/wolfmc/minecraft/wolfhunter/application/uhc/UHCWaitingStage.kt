package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.domain.event.CountdownFinished
import cn.wolfmc.minecraft.wolfhunter.domain.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.AutomaticGameStarter
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.setBorder
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.setRespawnRadius
import org.bukkit.Bukkit
import org.bukkit.Difficulty

object UHCWaitingStage : ScopeService {

    private val gameStarter: ScopeService = AutomaticGameStarter

    override fun init() {
        gameStarter.init()
    }

    override fun enable() {
        Bukkit.getWorlds().forEach {
            it.setRespawnRadius(24)
            it.setBorder(50.0)
            it.difficulty = Difficulty.PEACEFUL
        }
        // 计时开始
        gameStarter.enable()
        // 监听计时结束
        subscribe<CountdownFinished> {
            if (it.counter is AutomaticGameStarter) gameStarter.disable()
        }
    }

    override fun disable() {}
}
