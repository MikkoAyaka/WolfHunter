package cn.wolfmc.minecraft.wolfhunter.domain.model.game

import kotlinx.coroutines.Job

interface TimeCounter {
    var counter: Int
    var job: Job?
}
