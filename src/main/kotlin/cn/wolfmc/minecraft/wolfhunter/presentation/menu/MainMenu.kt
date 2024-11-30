package cn.wolfmc.minecraft.wolfhunter.presentation.menu

import cn.wolfmc.minecraft.wolfhunter.common.extensions.item
import cn.wolfmc.minecraft.wolfhunter.common.extensions.menu
import org.bukkit.Material

val mainMenu =
    menu("<blue>猎枪与獠牙</blue> <black>主菜单</black>", 36) {
        item(10) {
            material = Material.GLOBE_BANNER_PATTERN
            displayName = "<green>阵营</green> ${System.currentTimeMillis()}"
            lore = listOf("<gray>不同的势力与派系将为玩家带来不同的能力与游戏体验</gray>")
            refreshPeriod = 20
            leftClick = { this.sendMessage("Test Click") }
        }
    }
