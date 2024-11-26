package cn.wolfmc.minecraft.wolfhunter.presentation.command

import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CommandManager {
    private val commands = mutableMapOf<String, CommandExecutor>()
    
    fun register(name: String, executor: CommandExecutor) {}
    
    fun execute(name: String, sender: CommandSender, args: Array<String>): Boolean {
        return false
    }
} 