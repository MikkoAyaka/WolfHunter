package cn.wolfmc.minecraft.wolfhunter.presentation.item

import cn.wolfmc.minecraft.wolfhunter.common.extensions.legacy
import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import org.bukkit.Material
import taboolib.platform.util.buildItem

val scaffoldBlockTemplate =
    buildItem(Material.WHITE_WOOL) {
        lore.addAll(
            listOf(
                "",
                "  <gray>能够自动放置的辅助方块，需要手持使用，",
                "  <gray>放置该方块会消耗团队仓库或个人背包中的材料。",
                "",
                "  <green>左键 <gray>切换放置模式",
                "",
            ).map { str -> str.miniMsg().legacy() },
        )
    }

val growthArmorTemplate =
    buildItem(Material.BARRIER) {
        lore.addAll(
            listOf(
                "",
                "  下一等级 %wolfhunter_item_{uuid}_nextitem%",
                "  进化进度 %wolfhunter_item_{uuid}_bar%",
                "",
            ).map { str -> str.miniMsg().legacy() },
        )
    }
