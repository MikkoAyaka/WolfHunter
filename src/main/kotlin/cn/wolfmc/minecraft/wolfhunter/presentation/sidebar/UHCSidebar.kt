package cn.wolfmc.minecraft.wolfhunter.presentation.sidebar

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.common.extensions.EventHandler
import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.model.component.EventHandlerSet
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import net.kyori.adventure.text.Component
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.CollectionSidebarAnimation
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.SidebarAnimation
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor

object UHCSidebar : ScopeService {
    private lateinit var layout: ComponentSidebarLayout
    private lateinit var titleAnimation: SidebarAnimation<Component>
    private var sidebar: Sidebar? = null
    private val eventHandlers = EventHandlerSet()

    override fun init() {
        titleAnimation = gradientAnimation("猎枪与獠牙")
        val title = SidebarComponent.animatedLine(titleAnimation)
        val lines =
            SidebarComponent
                .builder()
                .addBlankLine()
                .addDynamicLine { "<gray> 剩余队伍 <white>%wolfhunter_teams%</white> 支".miniMsg() }
                .addDynamicLine { "<gray> 当前队伍 %wolfhunter_team_name% (剩余<white>%wolfhunter_team_players%</white>人)".miniMsg() }
                .addDynamicLine { "<gray> 世界中心 <white>%wolfhunter_center_pointer%</white>".miniMsg() }
                .addBlankLine()
                .build()
        layout = ComponentSidebarLayout(title, lines)

        eventHandlers +=
            EventHandler(PlayerJoinEvent::class) {
                sidebar?.addPlayer(it.player)
            }
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

    private var job: PlatformExecutor.PlatformTask? = null

    override fun enable() {
        sidebar = Contexts.scoreboardLib.createSidebar()
        eventHandlers.registerAll()
        sidebar!!.addPlayers(onlinePlayers())
        job =
            submit(async = true, period = 1) {
                titleAnimation.nextFrame()
                layout.apply(sidebar!!)
            }
    }

    override fun disable() {
        eventHandlers.unregisterAll()
        job?.cancel()
        sidebar?.close()
    }
}
