package cn.wolfmc.minecraft.wolfhunter.common.extensions

import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.java.JavaPlugin

// 扩展函数：为主命令创建 DSL
fun JavaPlugin.command(name: String, builder: CommandBuilder.() -> Unit) {
    val pluginCommand: PluginCommand = getCommand(name) ?: throw IllegalArgumentException("Command $name not found in plugin.yml")
    val commandBuilder = CommandBuilder(name).apply(builder)
    pluginCommand.setExecutor(commandBuilder.toExecutor())
}

// 核心命令构建器
class CommandBuilder(private val name: String) {
    private var executeBlock: (CommandSender.() -> Unit)? = null
    private val subcommands = mutableMapOf<String, CommandBuilder>()
    var argumentHandler: ((CommandSender, List<String>) -> Unit)? = null

    // 定义命令执行逻辑
    fun runs(block: CommandSender.() -> Unit) {
        executeBlock = block
    }

    // 定义子命令
    fun literal(sub: String, builder: CommandBuilder.() -> Unit) {
        subcommands[sub] = CommandBuilder(sub).apply(builder)
    }

    // 定义参数处理
    inline fun <reified T> argument(name: String, crossinline builder: ArgumentHandler<T>.(arg: T) -> Unit) {
        argumentHandler = { sender, args ->
            val resolvedValue = defaultResolver<T>()(args)
            val handler = ArgumentHandler(name, resolvedValue)
            handler.builder(resolvedValue)
            handler.execute(sender)
        }
    }

    // 转换成 CommandExecutor 并处理子命令和参数
    fun toExecutor(): CommandExecutor = CommandExecutor { sender, command, label, args ->
        if (args.isEmpty()) {
            executeBlock?.invoke(sender)
            return@CommandExecutor true
        }

        // 处理子命令递归调用
        val subcommand = subcommands[args[0]]
        if (subcommand != null) {
            subcommand.toExecutor().onCommand(sender, command, label, args.drop(1).toTypedArray())
            return@CommandExecutor true
        }

        // 处理参数
        argumentHandler?.invoke(sender, args.toList()) ?: executeBlock?.invoke(sender)
        true
    }
}

// 参数解析器类型别名
typealias ArgumentResolver<T> = (List<String>) -> T

// 默认的参数解析器
inline fun <reified T> defaultResolver(): ArgumentResolver<T> = { args ->
    when (T::class) {
        String::class -> args.firstOrNull() as? T ?: throw IllegalArgumentException("Expected a String")
        Int::class -> args.firstOrNull()?.toInt() as? T ?: throw IllegalArgumentException("Expected an Int")
        Double::class -> args.firstOrNull()?.toDouble() as? T ?: throw IllegalArgumentException("Expected a Double")
        else -> throw IllegalArgumentException("Unsupported type")
    }
}

// 参数处理器类
class ArgumentHandler<T>(val name: String, private val resolvedValue: T) {
    private var block: (CommandSender.(arg: T) -> Unit)? = null

    // 定义参数处理逻辑
    fun runs(block: CommandSender.(arg: T) -> Unit) {
        this.block = block
    }

    // 执行参数处理
    fun execute(sender: CommandSender) {
        block?.invoke(sender, resolvedValue)
    }
}
