package cn.wolfmc.minecraft.wolfhunter.presentation.menu

import cn.wolfmc.minecraft.wolfhunter.common.extensions.legacy
import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import cn.wolfmc.minecraft.wolfhunter.common.extensions.onLClick
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import taboolib.library.xseries.XMaterial
import taboolib.module.ui.buildMenu
import taboolib.module.ui.type.Chest
import taboolib.platform.util.buildItem

object MainMenu {
    val inventory: Inventory by lazy { build() }

    private fun build(): Inventory =
        buildMenu<Chest>("<#0451ad>[猎枪与獠牙] <black>主菜单</black>".miniMsg().legacy()) {
            map(
                "         ",
                " A       ",
                "         ",
            )
            set(
                'A',
                buildItem(XMaterial.GLOBE_BANNER_PATTERN) {
                    material = Material.GLOBE_BANNER_PATTERN
                    name = "<green>阵营</green>".miniMsg().legacy()
                    lore.add("<gray>不同的势力与派系将为玩家带来不同的能力与游戏体验</gray>".miniMsg().legacy())
                    colored()
                },
            ) {
                onLClick { it.sendMessage("Test") }
            }
        }
}
