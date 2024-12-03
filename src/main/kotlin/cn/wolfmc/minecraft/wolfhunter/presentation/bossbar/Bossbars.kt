package cn.wolfmc.minecraft.wolfhunter.presentation.bossbar

import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.wait
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.component.GameState
import cn.wolfmc.minecraft.wolfhunter.model.component.TimeCounter
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBar.Color
import net.kyori.adventure.bossbar.BossBar.Overlay
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.expansion.chain

class NumberBossBar(
    val bar: BossBar,
    private val title: () -> String,
    private val min: Int = 0,
    private val max: Int = 100,
    private val visibleCondition: Player.() -> Boolean = { true },
    val current: () -> Int,
) {
    init {
        onlinePlayers().forEach {
            if (it.visibleCondition()) {
                it.showBossBar(bar)
            } else {
                it.hideBossBar(bar)
            }
        }
        subscribe(PlayerJoinEvent::class) {
            if (it.player.visibleCondition()) {
                println("Show boss bar on join")
                it.player.showBossBar(bar)
            } else {
                it.player.hideBossBar(bar)
            }
        }
        chain {
            while (true) {
                wait(20)
                updateProgress()
                updateTitle()
            }
        }
    }

    private fun updateTitle() {
        bar.name(title().miniMsg())
    }

    private fun updateProgress() {
        val now = current()
        if (now > max) {
            bar.progress(1f)
        } else if (now < min) {
            bar.progress(0f)
        } else {
            bar.progress(now / max.toFloat())
        }
    }
}

fun bossBar(
    progress: Float,
    color: Color,
    overlay: Overlay,
) = BossBar.bossBar("".miniMsg(), progress, color, overlay)

fun gameStarterBossBar(
    timeCounter: TimeCounter,
    max: Int,
) = NumberBossBar(
    bossBar(1f, BossBar.Color.BLUE, BossBar.Overlay.NOTCHED_12),
    { "<white>游戏即将在 <green>${timeCounter.counter}</green> 秒后开始 ...</white>" },
    0,
    max,
    visibleCondition = { GameInstance.state == GameState.STARTING },
    current = { timeCounter.counter },
)
