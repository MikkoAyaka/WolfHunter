package cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler

import cn.wolfmc.minecraft.wolfhunter.common.constants.GrowthGearLevel
import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import cn.wolfmc.minecraft.wolfhunter.model.data.SpecialItem.GrowthGear
import cn.wolfmc.minecraft.wolfhunter.presentation.animation.TextBar
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object GrowthGearHandler : SpecialItemHandler<GrowthGear>() {
    override fun buildSpecialItem(itemStack: ItemStack): GrowthGear = GrowthGear(itemStack.type, itemStack.itemMeta)

    private val loreTemplate =
        """
        |  
        |  <gray>下一等级 %s
        |  <gray>升级进度 <white>%d</white> <gray> %s </gray> <white>%d</white>
        |  
        |  <gray>武器主要通过 <gold>战斗</gold> 获取经验</gray>
        |  <gray>工具主要通过 <green>采集</green> 获取经验</gray>
        |  <gray>护甲主要通过 <aqua>锻造</aqua> 获取经验(将铁锭拖拽到护甲以锻造)</gray>
        |  
        """.trimMargin()

    override fun updateItem(
        player: Player,
        specialItem: GrowthGear,
    ) {
        val level = specialItem.getLevel()
        val levelEnum = GrowthGearLevel.entries.getOrNull(level) ?: throw IllegalArgumentException("Unsupported growth level: $level")
        specialItem.apply {
            // 修正材质
            val levelType = levelEnum.getMaterial(material)
            if (material != levelType) material = levelType
            // 更新名字
            displayName("${levelEnum.color}<lang:${specialItem.material.translationKey()}>".miniMsg())
            // 更新描述
            lore(dynamicLore(specialItem))
        }
    }

    override fun updateItem(
        player: Player,
        specialItem: GrowthGear,
        latestItem: ItemStack,
    ) {
        super.updateItem(player, specialItem, latestItem)
        if (latestItem.type != specialItem.material) {
            latestItem.type = specialItem.material
        }
    }

    private fun dynamicLore(specialItem: GrowthGear): List<Component> {
        val material = specialItem.material
        val level = specialItem.getLevel()
        val nextLevelEnum = GrowthGearLevel.entries.getOrNull(level + 1)
        val translationKey = nextLevelEnum?.getMaterial(material)?.translationKey()
        val nextLevelDesc = if (translationKey == null) "<red>已达到最大等级" else "${nextLevelEnum.color}<lang:$translationKey>"
        val progressBar = TextBar.defaultStyle(specialItem.getLevelExpPercent(), 20)
        val levelRange = specialItem.getLevelExpRange()
        return loreTemplate.format(nextLevelDesc, levelRange.first, progressBar, levelRange.last).lines().map { it.miniMsg() }
    }

    override fun initItem(itemStack: ItemStack): GrowthGear {
        val specialItem = super.initItem(itemStack)
        // 初始化物品的特殊属性
        specialItem.apply {
            displayName("<lang:${itemStack.type.translationKey()}>".miniMsg())
            isUnbreakable = true
        }
        return specialItem
    }
}
