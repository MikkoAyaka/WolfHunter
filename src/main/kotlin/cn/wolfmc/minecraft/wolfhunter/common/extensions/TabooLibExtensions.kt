package cn.wolfmc.minecraft.wolfhunter.common.extensions

import kotlinx.coroutines.withContext
import taboolib.expansion.AsyncDispatcher
import taboolib.expansion.Chain
import taboolib.expansion.DurationType
import java.util.concurrent.CompletableFuture

suspend fun Chain<*>.wait(tick: Long) = wait(tick, DurationType.MINECRAFT_TICK)

// 命名 async 会冲突，无法使用，只能用 launch
suspend fun <T> Chain<*>.launch(block: suspend () -> T) = withContext(AsyncDispatcher) { block() }

typealias TBJob = CompletableFuture<*>

fun TBJob.cancel() = cancel(true)
