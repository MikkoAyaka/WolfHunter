package cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler

import cn.wolfmc.minecraft.wolfhunter.common.constants.NamespacedKeys
import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import cn.wolfmc.minecraft.wolfhunter.model.data.SpecialItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

/**
 * 主要负责发放物品、更新物品状态（名字、描述、数量）、管理物品数据
 */
abstract class SpecialItemHandler<T : SpecialItem> {
    private val thisItems = mutableMapOf<UUID, T>()

    fun allItems() = thisItems.values.toList()

    protected abstract fun buildSpecialItem(itemStack: ItemStack): T

    fun initItem(itemStack: ItemStack): T {
        val specialItem = buildSpecialItem(itemStack)
        // 刻录 UUID 到物品持久化容器中，方便读取
        itemStack.itemMeta =
            itemStack.itemMeta.apply {
                isUnbreakable = true
                persistentDataContainer.set(NamespacedKeys.UUID, PersistentDataType.STRING, specialItem.uuid.toString())
            }
        thisItems[specialItem.uuid] = specialItem
        return specialItem
    }

    /**
     * 除了更新元数据以外还会检查物品是否同步
     */
    open fun updateItem(
        player: Player,
        specialItem: T,
        latestItem: ItemStack,
    ) {
        updateItem(player, specialItem)
        latestItem.itemMeta =
            latestItem.itemMeta.apply {
                displayName(specialItem.name.miniMsg())
                lore(specialItem.lore.map { it.miniMsg() })
            }
        latestItem.amount = specialItem.amount
        latestItem.type = specialItem.type
    }

    /**
     * 只更新元数据，不更新物品
     */
    protected abstract fun updateItem(
        player: Player,
        specialItem: T,
    )

    fun has(item: ItemStack): Boolean = get(item) != null

    fun get(itemStack: ItemStack): T? {
        val uuidStr = itemStack.itemMeta?.persistentDataContainer?.get(NamespacedKeys.UUID, PersistentDataType.STRING) ?: return null
        val uuid = UUID.fromString(uuidStr)
        return thisItems[uuid]
    }
}
