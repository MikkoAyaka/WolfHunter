package cn.wolfmc.minecraft.wolfhunter.presentation.command

import cn.wolfmc.minecraft.wolfhunter.domain.service.GameService
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class WolfHunterCommand(
    private val gameService: GameService
) : CommandExecutor, TabCompleter {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (args.isEmpty()) {
            return false
        }
        
        when (args[0].lowercase()) {
            "start" -> gameService.startGame()
            "end" -> gameService.endGame()
            else -> return false
        }
        
        return true
    }
    
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String> {
        if (args.size == 1) {
            return listOf("start", "end")
        }
        return emptyList()
    }
} 