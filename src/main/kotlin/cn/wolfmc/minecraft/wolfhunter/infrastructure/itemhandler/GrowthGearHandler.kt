package cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler

import cn.wolfmc.minecraft.wolfhunter.model.data.SpecialItem.GrowthGear
import org.bukkit.inventory.ItemStack

object GrowthGearHandler : SpecialItemHandler<GrowthGear>() {
    override fun buildSpecialItem(itemStack: ItemStack): GrowthGear = GrowthGear(itemStack)
}
