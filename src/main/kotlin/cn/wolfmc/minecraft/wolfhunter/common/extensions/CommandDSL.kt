package cn.wolfmc.minecraft.wolfhunter.common.extensions

import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.java.JavaPlugin

typealias CommandSenderRunnable = CommandSender.() -> Unit
typealias CommandSenderConsumer = CommandSender.(arg: String) -> Unit
typealias CommandBuilderApplier = CommandBuilder.() -> Unit

fun JavaPlugin.command(name: String, builder: CommandBuilderApplier) {
    val pluginCommand: PluginCommand =
        getCommand(name) ?: throw IllegalArgumentException("Command $name not found in plugin.yml")
    val commandBuilder = CommandBuilder(name).apply(builder)
    pluginCommand.setExecutor(commandBuilder.toExecutor())
}

class CommandBuilder(private val name: String) {
    private var consumer: CommandSenderConsumer? = null
    private var runnable: CommandSenderRunnable? = null
    private val subcommands = mutableMapOf<String, CommandBuilder>()
    private val argumentConsumers = mutableMapOf<String, CommandSenderConsumer>()

    fun runs(block: CommandSenderRunnable) {
        runnable = block
    }

    fun literal(sub: String, runnable: CommandBuilderApplier) {
        subcommands[sub] = CommandBuilder(sub).apply(runnable)
    }

    fun argument(name: String = "", consumer: CommandSenderConsumer) {
        argumentConsumers[name] = { arg -> this.consumer(arg) }
    }

    fun toExecutor(): CommandExecutor = CommandExecutor { sender, command, label, args ->
        if (args.isEmpty()) {
            runnable?.invoke(sender)
            return@CommandExecutor true
        }

        val nowArg = args[0]
        val nextArg = args.getOrNull(1)

        val subcommand = subcommands[nowArg]
        if (subcommand != null) {
            subcommand.toExecutor().onCommand(sender, command, label, args.drop(1).toTypedArray())
            return@CommandExecutor true
        }

        if (argumentConsumers.containsKey(nowArg)) {
            if (nextArg == null) {
                sender.sendMessage("require argument")
                return@CommandExecutor false
            }
            argumentConsumers[nowArg]?.invoke(sender, nextArg)
            return@CommandExecutor true
        }
        if (argumentConsumers.containsKey("")) {
            argumentConsumers[""]?.invoke(sender, nowArg)
            return@CommandExecutor true
        }
        false
    }
}

