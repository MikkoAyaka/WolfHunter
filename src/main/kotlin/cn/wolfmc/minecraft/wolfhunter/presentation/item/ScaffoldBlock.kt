package cn.wolfmc.minecraft.wolfhunter.presentation.item

import cn.wolfmc.minecraft.wolfhunter.common.constants.surfaceVectors
import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ScaffoldBlock {
    // 激活自动效果的玩家
    private val activatedPlayers = mutableSetOf<Player>()

    init {
        PluginScope.launch {
            val surfaces = surfaceVectors()
            // 不要上面
            surfaces.removeAt(0)
            while (true) {
                delay(125)
                activatedPlayers.forEach { p ->
                    val placeLoc = p.location.clone().add(0.0, -1.0, 0.0)
                    val block = placeLoc.block
                    if (!block.isPassable && !block.type.isAir) return@forEach
                    // 主手副手都没拿，不生效
                    if (!isSimilar(p.inventory.itemInMainHand) && !isSimilar(p.inventory.itemInOffHand)) return@forEach
                    // 没有可放置的接触面
                    if (surfaces.map { placeLoc.clone().add(it).block }.none { it.isSolid }) return@forEach
                    // TODO
                    PluginScope.main {
                        block.type = Material.WHITE_WOOL
                        block.world.playSound(
                            block.location,
                            block.type
                                .createBlockData()
                                .soundGroup.placeSound,
                            1f,
                            1f,
                        )
                    }
                }
            }
        }
    }

    private val descriptions =
        listOf(
            "",
            "  <gray>能够自动放置的辅助方块，需要手持使用，",
            "  <gray>放置该方块会消耗团队仓库或个人背包中的材料。",
            "",
            "  <green>左键 <gray>切换放置模式",
            "",
        )

    private fun dynamicName(
        count: Int,
        enable: Boolean,
    ): String {
        val mode = if (enable) "<green>自动" else "<gray>手动"
        return "<reset><white>辅助方块 $mode (${count}个)</white>"
    }

    private val templateItem: ItemStack =
        itemStack {
            displayName = dynamicName(1, enable = false)
            material = Material.WHITE_WOOL
            amount = 64
            lore = descriptions
        }

    fun giveItem(player: Player) {
        player.giveItemSafely(templateItem)
    }

    fun updateItem(
        player: Player,
        scaffoldBlock: ItemStack,
    ) {
        scaffoldBlock.itemMeta =
            scaffoldBlock.itemMeta.apply {
                displayName(dynamicName(64, enable = activatedPlayers.contains(player)).miniMsg())
            }
        // TODO
        scaffoldBlock.amount = 64
    }

    fun toggle(
        player: Player,
        scaffoldBlock: ItemStack,
    ) {
        if (activatedPlayers.contains(player)) {
            activatedPlayers.remove(player)
        } else {
            activatedPlayers.add(player)
        }
        updateItem(player, scaffoldBlock)
    }

    /**
     * 特殊的比较方式
     * 因为物品名称和数量都可能会变化
     * 只能根据描述判断是否是该物品
     */
    fun isSimilar(itemStack: ItemStack): Boolean {
        if (!itemStack.hasItemMeta()) return false
        val itemLores = itemStack.lore()?.joinToString("\n") { it.plain() } ?: return false
        val templateLores = templateItem.lore()?.joinToString("\n") { it.plain() } ?: return false
        return itemLores == templateLores
    }
}
