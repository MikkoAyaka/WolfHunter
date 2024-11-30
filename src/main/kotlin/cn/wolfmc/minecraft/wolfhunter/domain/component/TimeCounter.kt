package cn.wolfmc.minecraft.wolfhunter.domain.component

import java.util.concurrent.CompletableFuture

interface TimeCounter {
    var counter: Int
    var future: CompletableFuture<*>?
}
