package cn.wolfmc.minecraft.wolfhunter.infrastructure.game

import cn.wolfmc.minecraft.wolfhunter.common.extensions.PluginScope
import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.domain.event.CountdownFinished
import cn.wolfmc.minecraft.wolfhunter.domain.model.game.TimeCounter
import cn.wolfmc.minecraft.wolfhunter.domain.service.ScopeService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

object AutomaticGameStarter : ScopeService, TimeCounter {

    override var counter = 600
    override var job: Job? = null

    override fun init() {}

    override fun enable() {
        if (job != null) return
        job =
            PluginScope.launch {
                while (true) {
                    delay(1000)
                    val playerAmount = onlinePlayers().size
                    if (playerAmount < 4) counter = 600 else counter--
                    if (playerAmount >= 8 && counter > 300) counter = 300
                    if (playerAmount >= 16 && counter > 60) counter = 60
                    if (counter <= 0) break
                }
                CountdownFinished(this@AutomaticGameStarter).callEvent()
            }
    }

    override fun disable() {}
}

object ReadyGameStarter : ScopeService, TimeCounter {
    override fun init() {
        TODO("Not yet implemented")
    }

    override fun enable() {
        TODO("Not yet implemented")
    }

    override fun disable() {
        TODO("Not yet implemented")
    }

    override var counter: Int = 600
    override var job: Job? = null
}
