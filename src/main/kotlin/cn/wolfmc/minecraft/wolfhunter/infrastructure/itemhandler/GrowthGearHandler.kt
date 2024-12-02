package cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler

import cn.wolfmc.minecraft.wolfhunter.common.constants.GrowthGearLevel
import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import cn.wolfmc.minecraft.wolfhunter.common.extensions.plain
import cn.wolfmc.minecraft.wolfhunter.model.data.SpecialItem.GrowthGear
import cn.wolfmc.minecraft.wolfhunter.presentation.animation.TextBar
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object GrowthGearHandler : SpecialItemHandler<GrowthGear>() {
    override fun buildSpecialItem(itemStack: ItemStack): GrowthGear = GrowthGear(itemStack)

    private val loreTemplate =
        """
        |  
        |  <gray>下一等级 %s
        |  <gray>升级进度 <white>%d</white> <gray> %s </gray> <white>%d</white>
        |  
        |  <gray>武器主要通过 <gold>战斗</gold> 获取经验</gray>
        |  <gray>工具主要通过 <green>采集</green> 获取经验</gray>
        |  
        """.trimMargin()

    override fun updateItem(
        player: Player?,
        specialItem: GrowthGear,
    ) {
        val level = specialItem.getLevel()
        val levelEnum = GrowthGearLevel.entries.getOrNull(level) ?: throw IllegalArgumentException("Unsupported growth level: $level")
        val material =
            specialItem.itemStack.apply {
                itemMeta =
                    itemMeta.apply {
                        // 修正材质
                        val levelType = levelEnum.getMaterial(type)
                        if (type != levelType) type = levelType
                        // 更新名字
                        displayName("${levelEnum.color}${specialItem.itemStack.displayName().plain()}".miniMsg())
                        // 更新描述
                        lore(dynamicLore(specialItem))
                    }
            }
    }

    private fun dynamicLore(specialItem: GrowthGear): List<Component> {
        val material = specialItem.itemStack.type
        val level = specialItem.getLevel()
        val nextLevelEnum = GrowthGearLevel.entries.getOrNull(level + 1)
        val translationKey = nextLevelEnum?.getMaterial(material)?.translationKey()
        val nextLevelDesc = if (translationKey == null) "<red>已达到最大等级" else "${nextLevelEnum.color}$translationKey"
        val progressBar = TextBar.defaultStyle(specialItem.getLevelExpPercent(), 20)
        val levelRange = specialItem.getLevelExpRange()
        return loreTemplate.format(nextLevelDesc, levelRange.first, progressBar, levelRange.last).lines().map { it.miniMsg() }
    }

    fun updateItem(specialItem: GrowthGear) {
        updateItem(null, specialItem)
    }

    override fun initItem(itemStack: ItemStack): GrowthGear {
        val specialItem = super.initItem(itemStack)
        // 初始化物品的特殊属性
        specialItem.itemStack.apply {
            itemMeta =
                itemMeta.apply {
                    isUnbreakable = true
                }
        }
        return specialItem
    }
}
