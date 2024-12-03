package cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler

import cn.wolfmc.minecraft.wolfhunter.common.constants.surfaceVectors
import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import cn.wolfmc.minecraft.wolfhunter.model.data.SpecialItem.ScaffoldBlock
import kotlinx.coroutines.delay
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.runTask
import taboolib.expansion.chain
import taboolib.platform.util.buildItem

object ScaffoldBlockHandler : SpecialItemHandler<ScaffoldBlock>() {
    // 激活自动效果的玩家
    private val activatedPlayers = mutableSetOf<Player>()

    override fun buildSpecialItem(itemStack: ItemStack): ScaffoldBlock =
        ScaffoldBlock(
            itemStack.type,
            itemStack.amount,
            dynamicName(itemStack.amount, false),
            listOf(
                "",
                "  <gray>能够自动放置的辅助方块，需要手持使用，",
                "  <gray>放置该方块会消耗团队仓库或个人背包中的材料。",
                "",
                "  <green>左键 <gray>切换放置模式",
                "",
            ),
        )

    init {
        // 自动搭路
        chain {
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
                    if (!p.inventory.itemInMainHand.isSpecialItem(ScaffoldBlockHandler) &&
                        !p.inventory.itemInOffHand.isSpecialItem(ScaffoldBlockHandler)
                    ) {
                        return@forEach
                    }
                    // 没有可放置的接触面
                    if (surfaces.map { placeLoc.clone().add(it).block }.none { it.isSolid }) return@forEach
                    // TODO
                    runTask {
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

    private fun dynamicName(
        count: Int,
        enable: Boolean,
    ): String {
        val mode = if (enable) "<green>自动" else "<gray>手动"
        return "<reset><white>辅助方块 $mode (${count}个)</white>"
    }

    fun giveItem(player: Player) {
        val itemStack =
            buildItem(Material.WHITE_WOOL) {
                amount = 64
            }
        val specialItem = initItem(itemStack)
        updateItem(player, specialItem, itemStack)
        player.giveItemSafely(itemStack)
    }

    override fun updateItem(
        player: Player,
        specialItem: ScaffoldBlock,
    ) {
        specialItem.apply {
            amount = 64
            name = dynamicName(64, enable = activatedPlayers.contains(player))
        }
    }

    override fun updateItem(
        player: Player,
        specialItem: ScaffoldBlock,
        latestItem: ItemStack,
    ) {
        super.updateItem(player, specialItem, latestItem)
        latestItem.amount = specialItem.amount
        latestItem.type = specialItem.type
    }

    fun toggle(
        player: Player,
        specialItem: ScaffoldBlock,
        itemStack: ItemStack,
    ) {
        if (activatedPlayers.contains(player)) {
            activatedPlayers.remove(player)
        } else {
            activatedPlayers.add(player)
        }
        updateItem(player, specialItem, itemStack)
    }
}
