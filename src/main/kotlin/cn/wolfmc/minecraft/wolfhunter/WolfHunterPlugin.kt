package cn.wolfmc.minecraft.wolfhunter

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.application.service.GlobalService
import cn.wolfmc.minecraft.wolfhunter.common.extensions.logT
import cn.wolfmc.minecraft.wolfhunter.presentation.command.registerCommands
import kotlinx.coroutines.GlobalScope
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class WolfHunterPlugin : JavaPlugin() {

    init {
        Contexts.plugin = this
    }

    override fun onLoad() {
        GlobalService.init()
    }

    override fun onEnable() {
        GlobalService.enable()
        logger.logT(Level.INFO,"plugin.enable")
    }
    
    override fun onDisable() {
        GlobalService.disable()
        logger.logT(Level.INFO,"plugin.disable")
    }
} 