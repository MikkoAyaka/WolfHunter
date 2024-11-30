package cn.wolfmc.minecraft.wolfhunter

import cn.wolfmc.minecraft.wolfhunter.application.AppService
import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import cn.wolfmc.minecraft.wolfhunter.presentation.listener.gameModeUpdater
import cn.wolfmc.minecraft.wolfhunter.presentation.listener.inventoryUpdater
import cn.wolfmc.minecraft.wolfhunter.presentation.listener.protectionUpdater
import taboolib.platform.BukkitPlugin
import java.util.logging.Level

object WolfHunterPlugin : BukkitPlugin() {
    override fun onLoad() {
        Contexts.plugin = this
        PluginScope.start()
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
