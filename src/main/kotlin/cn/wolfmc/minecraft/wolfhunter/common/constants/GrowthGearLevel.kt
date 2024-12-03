package cn.wolfmc.minecraft.wolfhunter.common.constants

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import org.bukkit.Material

/**
 * 顺序严格控制，从上到下对应最低级到最高级
 */
enum class GrowthGearLevel(
    val color: String,
    private val materialPrefix: String,
) {
    L1("<#FFFFFF>", "LEATHER"),
    L2("<#00FF7F>", "CHAINMAIL"),
    L3("<#00BFFF>", "IRON"),
    L4("<#FF00FF>", "DIAMOND"),
    L5("<#FF0000>", "NETHERITE"),
    ;

    private val cache: MutableMap<String, Material> = mutableMapOf()

    fun getMaterial(template: Material): Material {
        val args = template.name.split('_').toMutableList()
        if (args.size < 2) {
            Contexts.logger.warning("Growth Gear do not support the material: ${template.name}")
            return template
        }
        if (cache.containsKey(args[1])) return cache[args[1]]!!
        args[0] = this.materialPrefix
        val newMaterial = Material.getMaterial(args.joinToString(separator = "_"))
        if (newMaterial == null) {
            Contexts.logger.warning("Growth Gear do not support the material: ${template.name}")
            return template
        }
        cache[args[1]] = newMaterial
        return newMaterial
    }
}
