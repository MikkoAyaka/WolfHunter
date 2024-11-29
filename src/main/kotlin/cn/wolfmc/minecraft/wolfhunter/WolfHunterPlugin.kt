package cn.wolfmc.minecraft.wolfhunter

import cn.wolfmc.minecraft.wolfhunter.application.AppService
import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.common.extensions.MenuDSL
import cn.wolfmc.minecraft.wolfhunter.common.extensions.PluginScope
import cn.wolfmc.minecraft.wolfhunter.common.extensions.logT
import cn.wolfmc.minecraft.wolfhunter.common.extensions.register
import cn.wolfmc.minecraft.wolfhunter.presentation.listener.gameModeUpdater
import cn.wolfmc.minecraft.wolfhunter.presentation.listener.inventoryUpdater
import cn.wolfmc.minecraft.wolfhunter.presentation.listener.protectionUpdater
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File
import java.util.logging.Level

class WolfHunterPlugin : JavaPlugin {
    constructor() : super()

    constructor(
        loader: JavaPluginLoader,
        description: PluginDescriptionFile,
        dataFolder: File,
        file: File,
    ) : super(loader, description, dataFolder, file)

    init {
        Contexts.plugin = this
        PluginScope.start()
    }

    override fun onLoad() {
        AppService.init()
    }

    override fun onEnable() {
        initGlobalListener()
        AppService.enable()
        logger.logT(Level.INFO, "plugin.enable")
    }

    override fun onDisable() {
        AppService.disable()
        PluginScope.stop()
        logger.logT(Level.INFO, "plugin.disable")
    }

    private fun initGlobalListener() {
        MenuDSL.init(this)
        gameModeUpdater.registerAll()
        protectionUpdater.registerAll()
        inventoryUpdater.register()
    }
}
