package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts.worldMain
import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.WorldDevourer
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.setBorder
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.NoPortal
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.TeamSharedResource
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.sound.Sounds
import kotlinx.coroutines.delay
import org.bukkit.Difficulty
import org.bukkit.Location
import taboolib.common.platform.function.runTask
import taboolib.expansion.chain

object UHCRunningStage : ScopeService {
    private val stageMechanism = listOf(TeamSharedResource, NoPortal)

    override fun init() {
        stageMechanism.forEach { it.init() }
        worldDevourer.init()
    }

    override fun enable() {
        Contexts.apply {
            worldMain.worldBorder.setSize(500.0, 10)
            onlinePlayers().forEach {
                it.playSound(Sounds.THUNDER)
                it.playSound(Sounds.DRAGON_GROWL)
            }
            narrowBorder()
            worldMain.difficulty = Difficulty.HARD
            worldNether.setBorder(1.0)
            worldTheEnd.setBorder(1.0)
        }
        stageMechanism.forEach { it.enable() }
    }

    override fun disable() {
        borderTask?.cancel()
        stageMechanism.forEach { it.disable() }
        worldDevourer.disable()
    }

    private var borderTask: TBJob? = null
    private val worldDevourer =
        WorldDevourer(
            Location(worldMain, 0.0, 100.0, 0.0),
            30,
            worldMain.maxHeight,
            worldMain.minHeight,
            50..60,
            120,
        )

    private fun narrowBorder() {
        borderTask =
            chain {
                delay(1000 * 10)
                onlinePlayers().forEach { it.sendMessage("<green>距离世界边界收缩还有 120 秒。".miniMsg().plain()) }
                delay(1000 * 90)
                onlinePlayers().forEach { it.sendMessage("<yellow>距离世界边界收缩还有 30 秒。".miniMsg().plain()) }
                delay(1000 * 20)
                onlinePlayers().forEach { it.sendMessage("<red>边界即将缩小，请做好撤离的准备。".miniMsg().plain()) }
                delay(1000 * 10)
                runTask {
                    onlinePlayers().forEach { it.playSound(Sounds.THUNDER) }
                    worldMain.worldBorder.setSize(50.0, 60)
                }
                delay(1000 * 60)
                onlinePlayers().forEach { it.sendMessage("<red>虚空吞噬正从天空和地底步步逼近，小心脚下！".miniMsg().plain()) }
                worldDevourer.enable()
            }
    }
}
