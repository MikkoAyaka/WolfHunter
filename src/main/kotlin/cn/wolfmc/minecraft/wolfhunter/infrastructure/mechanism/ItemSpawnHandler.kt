package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.constants.isLog
import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.entity.ItemSpawnEvent

object ItemSpawnHandler : ScopeService {
    private val map =
        mutableMapOf<Material, Material>().apply {
            // 主世界
            put(Material.DIORITE, Material.COBBLESTONE)
            put(Material.GRANITE, Material.COBBLESTONE)
            put(Material.ANDESITE, Material.COBBLESTONE)
            put(Material.COBBLED_DEEPSLATE, Material.COBBLESTONE)
            put(Material.TUFF, Material.COBBLESTONE)
            put(Material.BASALT, Material.COBBLESTONE)
            put(Material.SANDSTONE, Material.SAND)
            // 下届
            put(Material.NETHER_BRICKS, Material.COBBLESTONE)
            put(Material.CRIMSON_NYLIUM, Material.NETHERRACK)
            put(Material.NETHER_WART_BLOCK, Material.NETHERRACK)
        }

    override fun init() {
    }

    private var listener: Listener? = null

    override fun enable() {
        listener =
            subscribe<ItemSpawnEvent> {
                val item = it.entity.itemStack
                if (item.type.isLog()) item.type = Material.OAK_LOG
                if (item.type in map.keys) {
                    item.type = map[item.type]!!
                }
            }
    }

    override fun disable() {
        listener?.unregister()
    }
}
