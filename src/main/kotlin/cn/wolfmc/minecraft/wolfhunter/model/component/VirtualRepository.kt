package cn.wolfmc.minecraft.wolfhunter.model.component

import cn.wolfmc.minecraft.wolfhunter.common.constants.VirtualResourceType
import cn.wolfmc.minecraft.wolfhunter.common.constants.isLog
import cn.wolfmc.minecraft.wolfhunter.common.constants.isMetal
import cn.wolfmc.minecraft.wolfhunter.common.constants.isStone
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class VirtualRepository {
    private val resourceMap = enumValues<VirtualResourceType>().associateWith { AtomicDouble(0.0) }

    @Synchronized fun add(
        type: VirtualResourceType,
        amount: Double,
    ) = resourceMap[type]!!.addAndGet(amount)

    fun get(type: VirtualResourceType): Double = resourceMap[type]!!.get()

    fun has(
        type: VirtualResourceType,
        amount: Double,
    ): Boolean = resourceMap[type]!!.get() >= amount

    @Synchronized fun store(
        player: Player,
        itemStack: ItemStack,
    ): Boolean {
        if (!isVirtualResource(itemStack.type)) return false
        interactVirtualItemHandler(itemStack)
        return true
    }

    @Synchronized fun take(
        type: VirtualResourceType,
        amount: Double,
    ): Boolean {
        if (!has(type, amount)) return false
        resourceMap[type]!!.addAndGet(-amount)
        return true
    }

    private fun interactVirtualItemHandler(itemStack: ItemStack) {
        when (val material = itemStack.type) {
            // 金属以铁的价值为参照
            Material.COPPER_INGOT -> add(VirtualResourceType.METAL, 0.2 * itemStack.amount)
            Material.IRON_INGOT -> add(VirtualResourceType.METAL, 1.0 * itemStack.amount)
            Material.GOLD_INGOT -> add(VirtualResourceType.METAL, 5.0 * itemStack.amount)
            Material.DIAMOND -> add(VirtualResourceType.METAL, 20.0 * itemStack.amount)
            Material.NETHERITE_INGOT -> add(VirtualResourceType.METAL, 120.0 * itemStack.amount)
            else -> {
                if (material.isStone()) add(VirtualResourceType.STONE, 1.0 * itemStack.amount)
                if (material.isLog()) add(VirtualResourceType.WOOD, 1.0 * itemStack.amount)
            }
        }
    }

    private fun isVirtualResource(material: Material) = material.isLog() || material.isStone() || material.isMetal()
}
