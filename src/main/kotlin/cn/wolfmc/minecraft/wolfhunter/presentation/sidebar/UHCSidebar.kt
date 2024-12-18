package cn.wolfmc.minecraft.wolfhunter.presentation.sidebar

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import net.kyori.adventure.text.Component
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.SidebarAnimation
import org.bukkit.entity.Player
import taboolib.platform.compat.replacePlaceholder

class UHCSidebar(
    private val player: Player,
    private val titleAnimation: SidebarAnimation<Component>,
) {
    private val sidebar =
        Contexts.scoreboardLib.createSidebar().apply {
            addPlayer(player)
        }

    private var layoutCache = layout()

    fun update() {
        layoutCache.apply(sidebar)
    }

    fun refreshCache() {
        layoutCache = layout()
    }

    private fun layout(): ComponentSidebarLayout {
        val title = SidebarComponent.animatedLine(titleAnimation)
        val lines =
            SidebarComponent
                .builder()
                .addBlankLine()
                .addStaticLine { "<gray> 剩余队伍 <white>%wolfhunter_teams%</white> 支".replacePlaceholder(player).miniMsg() }
                .addStaticLine {
                    "<gray> 当前队伍 %wolfhunter_team_name% (剩余<white>%wolfhunter_team_players%</white>人)"
                        .replacePlaceholder(
                            player,
                        ).miniMsg()
                }.addStaticLine { "<gray> 世界中心 <white>%wolfhunter_center_pointer%</white>".replacePlaceholder(player).miniMsg() }
                .addBlankLine()
                .build()
        return ComponentSidebarLayout(title, lines)
    }
}
