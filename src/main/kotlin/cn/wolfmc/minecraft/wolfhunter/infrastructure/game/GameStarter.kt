package cn.wolfmc.minecraft.wolfhunter.infrastructure.game

import cn.wolfmc.minecraft.wolfhunter.common.extensions.TBJob
import cn.wolfmc.minecraft.wolfhunter.common.extensions.cancel
import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.common.extensions.wait
import cn.wolfmc.minecraft.wolfhunter.model.component.TimeCounter
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent.CountdownFinished
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import taboolib.expansion.chain

object AutomaticGameStarter : ScopeService, TimeCounter {
    override var counter = 600
    override var future: TBJob? = null

    override fun init() {}

    override fun enable() {
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

    override fun disable() {
        future?.cancel()
        future = null
    }
}

/**
 * 玩家准备完毕后，游戏即将开始的倒计时器
 */
object ReadyCounter : ScopeService, TimeCounter {
    override fun init() {
    }

    override fun enable() {
        if (future != null) return
        future =
            chain {
                while (true) {
                    wait(20)
                    if (counter-- <= 0) break
                }
                CountdownFinished(this@ReadyCounter).callEvent()
            }
    }

    override fun disable() {
        future?.cancel()
        future = null
    }

    override var counter: Int = 15
    override var future: TBJob? = null
}
