package cn.wolfmc.minecraft.wolfhunter.model.component

import cn.wolfmc.minecraft.wolfhunter.common.extensions.legacy
import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import taboolib.module.ui.buildMenu
import taboolib.module.ui.type.StorableChest

class ChestRepository {
    val inventory =
        buildMenu<StorableChest>("<#0451ad>[猎枪与獠牙] <black>箱子仓库</black>".miniMsg().legacy()) {
            rows(3)
        }
}
