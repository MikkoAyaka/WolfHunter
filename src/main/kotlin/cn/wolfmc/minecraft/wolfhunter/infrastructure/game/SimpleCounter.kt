package cn.wolfmc.minecraft.wolfhunter.infrastructure.game

import cn.wolfmc.minecraft.wolfhunter.common.extensions.TBJob
import cn.wolfmc.minecraft.wolfhunter.common.extensions.cancel
import cn.wolfmc.minecraft.wolfhunter.common.extensions.wait
import cn.wolfmc.minecraft.wolfhunter.model.component.TimeCounter
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent.CountdownFinished
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import taboolib.expansion.chain

class SimpleCounter(
    override var counter: Int,
    val breakIfFinished: Boolean,
) : ScopeService,
    TimeCounter {
    override var future: TBJob? = null

    override fun init() {
    }

    override fun enable() {
        future =
            chain {
                while (true) {
                    wait(20)
                    if (counter-- <= 0) {
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
