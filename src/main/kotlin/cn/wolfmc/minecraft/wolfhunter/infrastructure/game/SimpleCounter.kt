package cn.wolfmc.minecraft.wolfhunter.infrastructure.game

import cn.wolfmc.minecraft.wolfhunter.common.extensions.TBJob
import cn.wolfmc.minecraft.wolfhunter.common.extensions.cancel
import cn.wolfmc.minecraft.wolfhunter.common.extensions.wait
import cn.wolfmc.minecraft.wolfhunter.model.component.TimeCounter
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent.CountdownFinished
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import taboolib.expansion.chain
import java.util.concurrent.atomic.AtomicInteger

class SimpleCounter(
    private val breakIfFinished: Boolean,
) : ScopeService,
    TimeCounter {
    override val current: AtomicInteger = AtomicInteger(999)
    override var future: TBJob? = null

    override fun init() {
    }

    override fun enable() {
        future =
            chain {
                while (true) {
                    wait(20)
                    if (current.getAndDecrement() <= 0) {
                        if (breakIfFinished) break
                    }
                }
                CountdownFinished(this@SimpleCounter).callEvent()
            }
    }

    override fun disable() {
        future?.cancel()
    }
}
