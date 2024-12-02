package cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler

import cn.wolfmc.minecraft.wolfhunter.common.constants.surfaceVectors
import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import cn.wolfmc.minecraft.wolfhunter.model.data.SpecialItem.ScaffoldBlock
import cn.wolfmc.minecraft.wolfhunter.presentation.item.scaffoldBlockTemplate
import kotlinx.coroutines.delay
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.runTask
import taboolib.expansion.chain

object ScaffoldBlockHandler : SpecialItemHandler<ScaffoldBlock>() {
    // 激活自动效果的玩家
    private val activatedPlayers = mutableSetOf<Player>()

    override fun buildSpecialItem(itemStack: ItemStack): ScaffoldBlock = ScaffoldBlock(itemStack)

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
        val specialItem = initItem(scaffoldBlockTemplate.clone())
        player.giveItemSafely(specialItem.itemStack)
        updateItem(player, specialItem)
    }

    fun updateItem(
        player: Player,
        specialItem: ScaffoldBlock,
    ) {
        specialItem.itemStack.itemMeta =
            specialItem.itemStack.itemMeta.apply {
                displayName(dynamicName(64, enable = activatedPlayers.contains(player)).miniMsg())
            }
        // TODO
        specialItem.itemStack.amount = 64
    }

    fun toggle(
        player: Player,
        specialItem: ScaffoldBlock,
    ) {
        if (activatedPlayers.contains(player)) {
            activatedPlayers.remove(player)
        } else {
            activatedPlayers.add(player)
        }
        updateItem(player, specialItem)
    }
}
