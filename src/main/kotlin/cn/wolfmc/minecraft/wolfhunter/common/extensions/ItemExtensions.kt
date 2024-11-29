package cn.wolfmc.minecraft.wolfhunter.common.extensions

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemStackBuilder {
    var material: Material = Material.STONE
    var displayName: String = ""
    var lore: List<String> = listOf()
    var amount: Int = 1

    fun build(): ItemStack {
        val item = ItemStack(material, amount)
        val meta: ItemMeta = item.itemMeta ?: throw IllegalArgumentException("ItemMeta is null")
        meta.displayName(displayName.miniMsg())
        meta.lore(lore.map { it.miniMsg() })
        item.itemMeta = meta
        return item
    }
}

fun itemStack(builder: ItemStackBuilder.() -> Unit): ItemStack = ItemStackBuilder().apply(builder).build()
