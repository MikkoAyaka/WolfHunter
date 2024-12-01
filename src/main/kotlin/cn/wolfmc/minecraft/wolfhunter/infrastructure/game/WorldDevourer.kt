package cn.wolfmc.minecraft.wolfhunter.infrastructure.game

import cn.wolfmc.minecraft.wolfhunter.common.extensions.TBJob
import cn.wolfmc.minecraft.wolfhunter.common.extensions.cancel
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import taboolib.common.platform.function.runTask
import taboolib.expansion.chain

class WorldDevourer(
    private val center: Location,
    private val radius: Int,
    private val fromTop: Int,
    private val fromBottom: Int,
    private val safeRange: IntRange,
    private val seconds: Int,
) : ScopeService {
    private var topIndex = 0
    private var bottomIndex = 0
    private var topTask: TBJob? = null
    private var bottomTask: TBJob? = null

    override fun init() {
        topIndex = fromTop
        bottomIndex = fromBottom
    }

    override fun enable() {
        val world = center.world ?: return

        // 顶部吞噬速度（间隔时间）
        val topInterval = seconds * 1000L / (fromTop - safeRange.last)

        // 底部吞噬速度（间隔时间）
        val bottomInterval = seconds * 1000L / (safeRange.first - fromBottom)

        // 开始顶部吞噬任务
        topTask =
            chain {
                while (topIndex > safeRange.last) {
                    kotlinx.coroutines.delay(topInterval)
                    devourLayer(world, topIndex)
                    topIndex--
                }
            }

        // 开始底部吞噬任务
        bottomTask =
            chain {
                while (bottomIndex < safeRange.first) {
                    kotlinx.coroutines.delay(bottomInterval)
                    devourLayer(world, bottomIndex)
                    bottomIndex++
                }
            }
    }

    override fun disable() {
        topTask?.cancel()
        bottomTask?.cancel()
    }

    private fun devourLayer(
        world: World,
        y: Int,
    ) {
        val cx = center.blockX
        val cz = center.blockZ

        for (x in (cx - radius)..(cx + radius)) {
            for (z in (cz - radius)..(cz + radius)) {
                val block = world.getBlockAt(x, y, z)
                runTask {
                    block.type = Material.AIR
                }
            }
        }
    }
}
