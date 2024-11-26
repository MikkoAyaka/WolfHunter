package cn.wolfmc.minecraft.wolfhunter.common.extensions

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun Player.giveItemSafely(item: ItemStack) {
    val leftover = inventory.addItem(item)
    if (leftover.isNotEmpty()) {
        world.dropItem(location, item)
    }
} 