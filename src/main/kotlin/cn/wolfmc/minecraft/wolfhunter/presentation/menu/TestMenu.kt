package cn.wolfmc.minecraft.wolfhunter.presentation.menu

import cn.wolfmc.minecraft.wolfhunter.common.extensions.item
import cn.wolfmc.minecraft.wolfhunter.common.extensions.menu
import org.bukkit.Material
import org.bukkit.inventory.Inventory

var testCount = 0
    get() = field++

val testMenu: Inventory
    get() =
        menu("<blue>猎枪与獠牙</blue> <black>第${testCount}个菜单</black>", 36) {
            item(10) {
                material = Material.GLOBE_BANNER_PATTERN
                displayName = "<green>阵营</green>"
                lore = listOf("<gray>不同的势力与派系将为玩家带来不同的能力与游戏体验</gray>")
                leftClick = { sendMessage("Test Click: $testCount") }
            }
        }
