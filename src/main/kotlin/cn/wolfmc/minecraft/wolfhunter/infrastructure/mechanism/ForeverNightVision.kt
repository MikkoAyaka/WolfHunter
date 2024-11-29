package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.PluginScope
import cn.wolfmc.minecraft.wolfhunter.common.extensions.create
import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.domain.service.ScopeService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.bukkit.potion.PotionEffectType

object ForeverNightVision : ScopeService {
    private var job: Job? = null

    override fun init() {}

    override fun enable() {
        job =
            PluginScope.async {
                while (true) {
                    delay(5000)
                    onlinePlayers().forEach {
                        PluginScope.main {
                            PotionEffectType.NIGHT_VISION.create(20 * 10, 0).apply(it)
                        }
                    }
                }
            }
    }

    override fun disable() {
        job?.cancel()
    }
}
