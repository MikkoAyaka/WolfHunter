package cn.wolfmc.minecraft.wolfhunter.common.extensions

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts.plugin
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import java.lang.Runnable
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

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

    fun main(block: suspend CoroutineScope.() -> Unit): Job = CoroutineScope(BukkitMainThreadDispatcher).launch(block = block)

    fun async(block: suspend CoroutineScope.() -> Unit): Job = CoroutineScope(coroutineContext).launch(block = block)
}

object BukkitMainThreadDispatcher : CoroutineDispatcher() {
    private val executor =
        Executor { command ->
            Bukkit.getScheduler().runTask(plugin, Runnable { command.run() })
        }

    override fun dispatch(
        context: CoroutineContext,
        block: Runnable,
    ) {
        executor.execute(block)
    }
}
