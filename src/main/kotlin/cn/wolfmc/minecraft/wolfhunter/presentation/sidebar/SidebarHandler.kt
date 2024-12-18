package cn.wolfmc.minecraft.wolfhunter.presentation.sidebar

import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import net.kyori.adventure.text.Component
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.CollectionSidebarAnimation
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.SidebarAnimation
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.expansion.chain
import java.util.concurrent.ConcurrentHashMap

object SidebarHandler : ScopeService {
    override fun init() {
    }

    // 标题共享
    private val titleAnimation: SidebarAnimation<Component> = gradientAnimation("猎枪与獠牙")

    private val sidebarMap = ConcurrentHashMap<Player, UHCSidebar>()
    private var mainJob: TBJob? = null
    private var titleJob: PlatformExecutor.PlatformTask? = null

    override fun enable() {
        mainJob =
            chain {
                while (true) {
                    wait(20)
                    onlinePlayers().forEach {
                        sidebarMap.getOrPut(it) { UHCSidebar(it, titleAnimation) }
                    }
                }
            }
        titleJob =
            submit(async = true, period = 1) {
                titleAnimation.nextFrame()
                sidebarMap.values.forEach {
                    it.update()
                }
            }
    }

    override fun disable() {
        mainJob?.cancel()
        titleJob?.cancel()
    }

    private fun gradientAnimation(text: String): SidebarAnimation<Component> {
        val step = 1f / 8f
        val frames: MutableList<Component> = ArrayList((2f / step).toInt())
        var phase = -1f
        while (phase < 1) {
            frames.add("<gradient:red:blue:$phase>$text".miniMsg())
            phase += step
        }
        return CollectionSidebarAnimation(frames)
    }
}
