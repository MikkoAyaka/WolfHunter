package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.function.runTask
import taboolib.expansion.chain

object ForeverNightVision : ScopeService {
    private var job: TBJob? = null

    override fun init() {}

    override fun enable() {
        job =
            chain {
                while (true) {
                    wait(20 * 5)
                    onlinePlayers().forEach {
                        runTask {
                            PotionEffectType.NIGHT_VISION
                                .create(20 * 30, 0)
                                .withIcon(false)
                                .withParticles(false)
                                .apply(it)
                        }
                    }
                }
            }
    }

    override fun disable() {
        job?.cancel()
    }
}
