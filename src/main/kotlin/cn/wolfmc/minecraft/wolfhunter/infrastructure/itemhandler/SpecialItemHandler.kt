package cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler

import cn.wolfmc.minecraft.wolfhunter.common.constants.NamespacedKeys
import cn.wolfmc.minecraft.wolfhunter.model.data.SpecialItem
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

/**
 * 主要负责发放物品、更新物品状态、管理物品数据
 */
abstract class SpecialItemHandler<T : SpecialItem> {
    private val thisItems = mutableMapOf<UUID, T>()

    protected abstract fun buildSpecialItem(itemStack: ItemStack): T

    fun initItem(itemStack: ItemStack): T {
        val specialItem = buildSpecialItem(itemStack)
        thisItems[specialItem.uuid] = specialItem
        // 刻录 UUID 到物品持久化容器中，方便读取
        itemStack.itemMeta =
            itemStack.itemMeta.apply {
                persistentDataContainer.set(NamespacedKeys.UUID, PersistentDataType.STRING, specialItem.uuid.toString())
            }
        return specialItem
    }

    fun has(item: ItemStack): Boolean = get(item) != null

    fun get(itemStack: ItemStack): T? {
        val uuidStr = itemStack.itemMeta.persistentDataContainer.get(NamespacedKeys.UUID, PersistentDataType.STRING) ?: return null
        val uuid = UUID.fromString(uuidStr)
        return thisItems[uuid]
    }
}
