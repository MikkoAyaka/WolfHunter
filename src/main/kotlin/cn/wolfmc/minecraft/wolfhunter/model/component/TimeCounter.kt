package cn.wolfmc.minecraft.wolfhunter.model.component

import cn.wolfmc.minecraft.wolfhunter.common.extensions.TBJob

interface TimeCounter {
    var counter: Int
    var future: TBJob?
}
