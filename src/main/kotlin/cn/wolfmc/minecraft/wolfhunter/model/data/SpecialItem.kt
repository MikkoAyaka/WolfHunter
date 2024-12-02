package cn.wolfmc.minecraft.wolfhunter.model.data

import org.bukkit.inventory.ItemStack
import java.util.UUID

sealed class SpecialItem(
    val itemStack: ItemStack,
) {
    val uuid = UUID.randomUUID()

    class ScaffoldBlock(
        itemStack: ItemStack,
    ) : SpecialItem(itemStack)

    class GrowthGear(
        itemStack: ItemStack,
    ) : SpecialItem(itemStack) {
        private var exp = 0

        @Synchronized fun addExp(value: Int) {
            exp += value
        }

        fun getExp() = exp

        fun getLevelExpRange(): IntRange {
            val level = getLevel()
            val min = levelUpRequireList[level]
            val max = levelUpRequireList.getOrNull(level + 1) ?: 99999
            return min..max
        }

        fun getLevelExpPercent(): Double {
            val range = getLevelExpRange()
            val relativeExp = exp - range.first
            val max = range.last - range.first
            return relativeExp / max.toDouble()
        }

        /**
         * 从等级 0 开始
         */
        fun getLevel(): Int {
            var level = 0
            levelUpRequireList.forEachIndexed { index, requireExp ->
                if (exp >= requireExp) level = index
            }
            return level
        }

        companion object {
            private val levelUpRequireList = listOf(0, 1000, 3000, 6000, 10000)
        }
    }
}
