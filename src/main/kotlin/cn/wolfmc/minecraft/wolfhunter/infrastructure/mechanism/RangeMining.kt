package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.extensions.nearbyBlocks
import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.block.Block
import org.bukkit.block.Container
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.roundToInt

object RangeMining : ScopeService {
    override fun init() {
    }

    private var listener: Listener? = null

    override fun enable() {
        listener =
            subscribe<BlockBreakEvent> {
                if (it.player.isSneaking) return@subscribe
                if (it.block is Container) return@subscribe
                rangeMining(it.player, it.player.inventory.itemInMainHand, it.block)
            }
    }

    override fun disable() {
        listener?.unregister()
    }

    private fun rangeMining(
        player: Player,
        tool: ItemStack,
        block: Block,
    ) {
        val direction = player.location.direction.normalize()
        val centerMaterial = block.type
        val centerHardness = centerMaterial.hardness
        block
            .nearbyBlocks(2)
            // 偏移
            .map { it.getRelative(direction.x.roundToInt(), direction.y.roundToInt(), direction.z.roundToInt()) }
            // 条件判断
            .filter { !it.isEmpty && it.type.hardness <= centerHardness && it !is Container }
            // 破坏
            .forEach { it.breakNaturally(tool) }
    }
}
