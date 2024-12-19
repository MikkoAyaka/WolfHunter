package cn.wolfmc.minecraft.wolfhunter.infrastructure.game

import cn.wolfmc.minecraft.wolfhunter.common.extensions.TBJob
import cn.wolfmc.minecraft.wolfhunter.common.extensions.cancel
import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.common.extensions.wait
import cn.wolfmc.minecraft.wolfhunter.model.component.TimeCounter
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent.CountdownFinished
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.sound.Sounds
import taboolib.common.platform.function.runTask
import taboolib.expansion.chain
import java.util.concurrent.atomic.AtomicInteger

object AutomaticGameStarter : ScopeService, TimeCounter {
    override val current = AtomicInteger(600)
    override var future: TBJob? = null

    override fun init() {}

    override fun enable() {
        future =
            chain {
                while (true) {
                    wait(20)
                    val playerAmount = onlinePlayers().size
                    val cur = current.get()
                    if (playerAmount < 4) current.set(600) else current.getAndDecrement()
                    if (playerAmount >= 8 && cur > 300) current.set(300)
                    if (playerAmount >= 16 && cur > 60) current.set(60)
                    if (cur <= 0) break
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
                    runTask {
                        onlinePlayers().forEach {
                            it.playSound(Sounds.PLING)
                        }
                    }
                    if (current.getAndDecrement() <= 0) break
                }
                CountdownFinished(this@ReadyCounter).callEvent()
            }
    }

    override fun disable() {
        future?.cancel()
        future = null
    }

    override var current = AtomicInteger(15)
    override var future: TBJob? = null
}
