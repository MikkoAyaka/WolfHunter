package cn.wolfmc.minecraft.wolfhunter.presentation.ui

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
    var title: () -> String,
    private val min: Int = 0,
    var max: Int = 100,
    private val visibleCondition: Player.() -> Boolean = { true },
    val current: () -> Int,
) {
    fun init() {
        subscribe(PlayerJoinEvent::class) {
            if (it.player.visibleCondition()) {
                it.player.showBossBar(bar)
            } else {
                it.player.hideBossBar(bar)
            }
        }
        chain {
            while (true) {
                wait(4)
                updateProgress()
                updateTitle()
                updatePlayers()
            }
        }
    }

    private fun updatePlayers() {
        onlinePlayers().forEach {
            if (it.visibleCondition()) {
                it.showBossBar(bar)
            } else {
                it.hideBossBar(bar)
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
    bossBar(1f, Color.BLUE, Overlay.NOTCHED_20),
    { "<white>游戏即将开始！</white><green>${timeCounter.current}</green>" },
    0,
    max,
    visibleCondition = { GameInstance.state == GameState.STARTING },
    current = { timeCounter.current.get() },
)

fun waitBossBar(
    timeCounter: TimeCounter,
    max: Int,
) = NumberBossBar(
    bossBar(1f, Color.GREEN, Overlay.NOTCHED_20),
    {
        if (timeCounter.current.get() == max) {
            "<white>正在等待玩家加入，需要至少 4 人</white>"
        } else {
            "<white>正在等待玩家加入，游戏将在 <green>${timeCounter.current}</green> 秒后开始</white>"
        }
    },
    0,
    max,
    visibleCondition = { GameInstance.state == GameState.WAITING },
    current = { timeCounter.current.get() },
)

fun progressBossBar(
    timeCounter: TimeCounter,
    max: Int,
) = NumberBossBar(
    bossBar(1f, Color.WHITE, Overlay.NOTCHED_20),
    title = { "" },
    min = 0,
    max = max,
    visibleCondition = { GameInstance.state == GameState.RUNNING },
    current = { timeCounter.current.get() },
)
