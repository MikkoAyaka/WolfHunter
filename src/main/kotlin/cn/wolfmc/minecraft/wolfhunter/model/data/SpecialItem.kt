package cn.wolfmc.minecraft.wolfhunter.model.data

import org.bukkit.inventory.ItemStack
import java.util.UUID

sealed class SpecialItem(
    val itemStack: ItemStack,
) {
    val uuid = UUID.randomUUID()

    class ScaffoldBlock(
        itemStack: ItemStack,
    ) : SpecialItem(itemStack)

    class GrowthGear(
        itemStack: ItemStack,
    ) : SpecialItem(itemStack)
}
