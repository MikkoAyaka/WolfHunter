package cn.wolfmc.minecraft.wolfhunter

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.lang.Language
import taboolib.module.lang.sendLang

object WolfHunterPlugin : Plugin() {
    override fun onLoad() {
        Language.enableSimpleComponent = true
    }

    override fun onEnable() {
        console().sendLang("enable")
    }

    override fun onDisable() {
        console().sendLang("disable")
    }
}
