package cn.wolfmc.minecraft.wolfhunter.infrastructure.kit

import cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler.ScaffoldBlockHandler
import cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.GrowthGear
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object UHCKit {
    private val content =
        mutableMapOf(
            0 to ItemStack(Material.WOODEN_SWORD),
            1 to ItemStack(Material.WOODEN_AXE),
            2 to ItemStack(Material.WOODEN_PICKAXE),
            3 to ItemStack(Material.BREAD, 8),
            4 to ItemStack(Material.OAK_PLANKS, 32),
            39 to ItemStack(Material.LEATHER_HELMET),
            38 to ItemStack(Material.LEATHER_CHESTPLATE),
            37 to ItemStack(Material.LEATHER_LEGGINGS),
            36 to ItemStack(Material.LEATHER_BOOTS),
        )

    fun give(player: Player) {
        content.forEach { (index, item) ->
            player.inventory.setItem(index, item)
        }
        player.inventory.forEach {
            GrowthGear.tryInitItem(player, it)
        }
        ScaffoldBlockHandler.giveItem(player)
    }
}
