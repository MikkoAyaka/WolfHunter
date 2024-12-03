package cn.wolfmc.minecraft.wolfhunter.model.data

import org.bukkit.Material
import java.util.UUID

sealed class SpecialItem(
    var type: Material,
    var amount: Int,
    var name: String,
    var lore: List<String>,
) {
    val uuid = UUID.randomUUID()!!

    class ScaffoldBlock(
        material: Material,
        amount: Int,
        name: String,
        lore: List<String>,
    ) : SpecialItem(material, amount, name, lore)

    class GrowthGear(
        material: Material,
        amount: Int,
        name: String,
        lore: List<String>,
    ) : SpecialItem(material, amount, name, lore) {
        private var exp = 0

        @Synchronized fun addExp(value: Double) {
            // TODO
            val multiple = cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism.GrowthGear.expMultiple
            exp += (value * multiple).toInt()
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
            private val levelUpRequireList = listOf(0, 1000, 4000, 10000, 20000)
        }
    }
}
