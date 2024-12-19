package cn.wolfmc.minecraft.wolfhunter.common.constants

import org.bukkit.Color

enum class VirtualResourceType(
    val displayName: String,
    val color: Color,
) {
    METAL("金属", Color.fromRGB(255, 215, 0)),
    STONE("石料", Color.fromRGB(112, 128, 144)),
    WOOD("木材", Color.fromRGB(34, 139, 34)),
}
