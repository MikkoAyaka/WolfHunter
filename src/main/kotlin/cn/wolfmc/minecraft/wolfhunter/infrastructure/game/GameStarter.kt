package cn.wolfmc.minecraft.wolfhunter.infrastructure.game

import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.common.extensions.wait
import cn.wolfmc.minecraft.wolfhunter.model.component.TimeCounter
import cn.wolfmc.minecraft.wolfhunter.model.event.CountdownFinished
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import taboolib.expansion.chain
import java.util.concurrent.CompletableFuture

object AutomaticGameStarter : ScopeService, TimeCounter {
    override var counter = 600
    override var future: CompletableFuture<*>? = null

    override fun init() {}

    override fun enable() {
        if (future != null) return
        future =
            chain {
                while (true) {
                    wait(20)
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
    override var future: CompletableFuture<*>? = null
}
