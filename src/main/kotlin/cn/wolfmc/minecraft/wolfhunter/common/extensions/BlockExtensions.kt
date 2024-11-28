package cn.wolfmc.minecraft.wolfhunter.common.extensions

import org.bukkit.block.Block

/**
 * radius: 1 单个方块
 * radius: 2 3x3x3 范围内方块
 * radius: 3 5x5x5 范围内方块
 */
fun Block.nearbyBlocks(radius: Int): Set<Block> {
    if (radius <= 1) return setOf(this)
    val extendRadius = radius - 1
    val blocks = mutableSetOf<Block>()
    for (x in -extendRadius..extendRadius) {
        for (y in -extendRadius..extendRadius) {
            for (z in -extendRadius..extendRadius) {
                blocks.add(location.clone().add(x.toDouble(), y.toDouble(), z.toDouble()).block)
            }
        }
    }
    return blocks
}