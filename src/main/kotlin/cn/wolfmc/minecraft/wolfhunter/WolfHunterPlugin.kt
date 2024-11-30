package cn.wolfmc.minecraft.wolfhunter

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import cn.wolfmc.minecraft.wolfhunter.presentation.listener.gameModeUpdater
import cn.wolfmc.minecraft.wolfhunter.presentation.listener.inventoryUpdater
import cn.wolfmc.minecraft.wolfhunter.presentation.listener.protectionUpdater
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.lang.Language
import taboolib.module.lang.sendLang

object WolfHunterPlugin : Plugin() {
    override fun onLoad() {
        Language.enableSimpleComponent = true
    }

    override fun onEnable() {
        initGlobalListener()
        console().sendLang("enable")
    }

    override fun onDisable() {
        console().sendLang("disable")
    }

    private fun initGlobalListener() {
        MenuDSL.init(Contexts.plugin)
        gameModeUpdater.registerAll()
        protectionUpdater.registerAll()
        inventoryUpdater.register()
    }
}
