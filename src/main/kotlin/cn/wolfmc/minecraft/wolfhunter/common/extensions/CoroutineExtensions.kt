package cn.wolfmc.minecraft.wolfhunter.common.extensions

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object PluginScope : CoroutineScope {
    private var job: Job? = null

    // 默认使用 Dispatchers.Default 作为调度器，结合 job 来控制协程生命周期
    override val coroutineContext: CoroutineContext
        get() = job?.let { it + Dispatchers.Default } ?: Dispatchers.Default

    // 启动插件时的协程 scope
    fun start() {
        job = Job() // 在启用时创建 Job，开始管理协程
    }

    // 停止插件时取消所有协程
    fun stop() {
        job?.cancel() // 在禁用时取消协程
    }

    fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        CoroutineScope(coroutineContext).launch(block = block)
}
