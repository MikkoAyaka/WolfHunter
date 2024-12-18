package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts.worldMain
import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.SimpleCounter
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.UHCGameJudge
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.WorldDevourer
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.setBorder
import cn.wolfmc.minecraft.wolfhunter.infrastructure.kit.UHCKit
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.NoPortal
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.TeamSharedResource
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.bossbar.progressBossBar
import cn.wolfmc.minecraft.wolfhunter.presentation.sound.Sounds
import kotlinx.coroutines.delay
import org.bukkit.Difficulty
import org.bukkit.Location
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.function.runTask
import taboolib.expansion.chain

object UHCRunningStage : ScopeService {
    private val stageMechanism = listOf(TeamSharedResource, NoPortal)

    override fun init() {
        stageMechanism.forEach { it.init() }
        worldDevourer.init()
        UHCGameJudge.init()
    }

    override fun enable() {
        Contexts.apply {
            worldMain.worldBorder.setSize(500.0, 10)
            onlinePlayers().forEach {
                it.playSound(Sounds.THUNDER)
                it.playSound(Sounds.DRAGON_GROWL)
                UHCKit.give(it)
                it.addPotionEffect(
                    PotionEffectType.DOLPHINS_GRACE
                        .createEffect(20 * 30, 0)
                        .withParticles(false)
                        .withIcon(true),
                )
                it.addPotionEffect(
                    PotionEffectType.WATER_BREATHING
                        .createEffect(20 * 30, 0)
                        .withParticles(false)
                        .withIcon(true),
                )
                it.addPotionEffect(
                    PotionEffectType.DAMAGE_RESISTANCE
                        .createEffect(20 * 30, 2)
                        .withParticles(false)
                        .withIcon(true),
                )
            }
            narrowBorder()
            worldMain.difficulty = Difficulty.HARD
            worldNether.setBorder(1.0)
            worldTheEnd.setBorder(1.0)
        }
        stageMechanism.forEach { it.enable() }
        UHCGameJudge.enable()
    }

    override fun disable() {
        UHCGameJudge.disable()
        borderTask?.cancel()
        stageMechanism.forEach { it.disable() }
        worldDevourer.disable()
    }

    private var borderTask: TBJob? = null
    private val worldDevourer =
        WorldDevourer(
            Location(worldMain, 0.0, 100.0, 0.0),
            10,
            worldMain.maxHeight,
            worldMain.minHeight,
            56..60,
            120,
        )

    private val progressCounter =
        SimpleCounter(30, false).apply {
            this.init()
            this.enable()
        }
    private val bar = progressBossBar(progressCounter.apply { counter = 130 }, 130)

    private fun narrowBorder() {
        borderTask =
            chain {
                bar.apply {
                    this.init()
                    title = { "<green>边界稳定中..." }
                }
                delay(1000 * 10)
                onlinePlayers().forEach { it.sendMessage("<green>距离世界边界收缩还有 120 秒。".miniMsg().legacy()) }
                delay(1000 * 90)
                onlinePlayers().forEach { it.sendMessage("<yellow>距离世界边界收缩还有 30 秒。".miniMsg().legacy()) }
                delay(1000 * 20)
                onlinePlayers().forEach { it.sendMessage("<red>边界即将缩小，请做好撤离的准备。".miniMsg().legacy()) }
                delay(1000 * 10)
                bar.apply {
                    progressCounter.counter = 300
                    max = 300
                    title = { "<yellow>边界正在收缩..." }
                }
                runTask {
                    onlinePlayers().forEach { it.playSound(Sounds.THUNDER) }
                    worldMain.worldBorder.setSize(50.0, 300)
                }
                delay(1000 * 300)
                bar.apply {
                    progressCounter.counter = 90
                    max = 90
                    title = { "<green>边界稳定中..." }
                }
                onlinePlayers().forEach { it.sendMessage("<white>边界将进一步缩小，准备好战斗吧！".miniMsg().legacy()) }
                delay(1000 * 90)
                bar.apply {
                    progressCounter.counter = 90
                    max = 90
                    title = { "<yellow>边界正在收缩..." }
                }
                runTask {
                    onlinePlayers().forEach { it.playSound(Sounds.THUNDER) }
                    worldMain.worldBorder.setSize(10.0, 90)
                }
                delay(1000 * 90)
                bar.apply {
                    progressCounter.counter = 300
                    max = 300
                    title = { "<red>世界开始坍塌！" }
                }
                onlinePlayers().forEach { it.sendMessage("<red>虚空吞噬正从天空和地底步步逼近，小心脚下！".miniMsg().legacy()) }
                worldDevourer.enable()
                onlinePlayers().forEach {
                    it.playSound(Sounds.WORLD_BREAK)
                }
            }
    }
}
