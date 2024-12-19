package cn.wolfmc.minecraft.wolfhunter.common.constants

import org.bukkit.util.Vector

/**
 * 上下(y)左右(x)前后(z) 相对坐标
 * 顺序不可以更改
 */
fun surfaceVectors() =
    mutableListOf(
        Vector(0, 1, 0), // y+
        Vector(0, -1, 0), // y-
        Vector(1, 0, 0), // x+
        Vector(-1, 0, 0), // x-
        Vector(0, 0, 1), // z+
        Vector(0, 0, -1), // z-
    )
