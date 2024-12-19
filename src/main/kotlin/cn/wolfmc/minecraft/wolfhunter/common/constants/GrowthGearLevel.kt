package cn.wolfmc.minecraft.wolfhunter.common.constants

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import org.bukkit.Material

/**
 * 顺序严格控制，从上到下对应最低级到最高级
 */
enum class GrowthGearLevel(
    val color: String,
    private val armorMaterialPrefix: String,
    private val toolMaterialPrefix: String,
) {
    L1("<#FFFFFF>", "LEATHER", "WOODEN"),
    L2("<#00FF7F>", "CHAINMAIL", "STONE"),
    L3("<#00BFFF>", "IRON", "IRON"),
    L4("<#FF00FF>", "DIAMOND", "DIAMOND"),
    L5("<#FF0000>", "NETHERITE", "NETHERITE"),
    ;

    private val cache: MutableMap<String, Material> = mutableMapOf()

    fun getMaterial(template: Material): Material {
        val args = template.name.split('_').toMutableList()
        if (args.size < 2) {
            Contexts.logger.warning("Growth Gear don't support the material: ${template.name}")
            return template
        }
        if (cache.containsKey(args[1])) return cache[args[1]]!!
        args[0] = if (template.isArmor()) this.armorMaterialPrefix else this.toolMaterialPrefix
        val newMaterial = Material.getMaterial(args.joinToString(separator = "_"))
        if (newMaterial == null) {
            Contexts.logger.warning("Growth Gear can't find material: ${args.joinToString(separator = "_")}")
            return template
        }
        cache[args[1]] = newMaterial
        return newMaterial
    }
}
