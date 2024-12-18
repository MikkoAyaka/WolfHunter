package cn.wolfmc.minecraft.wolfhunter.model.component

import cn.wolfmc.minecraft.wolfhunter.common.extensions.TBJob
import java.util.concurrent.atomic.AtomicInteger

interface TimeCounter {
    val current: AtomicInteger
    var future: TBJob?
}
