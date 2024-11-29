package cn.wolfmc.minecraft.wolfhunter.domain.component

import kotlinx.coroutines.Job

interface TimeCounter {
    var counter: Int
    var job: Job?
}
