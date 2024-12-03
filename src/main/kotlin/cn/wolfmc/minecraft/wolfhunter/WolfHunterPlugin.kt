package cn.wolfmc.minecraft.wolfhunter

import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.lang.Language
import taboolib.module.lang.sendLang

@RuntimeDependencies(
    RuntimeDependency(value = "net.megavex:scoreboard-library-api:2.2.1"),
    RuntimeDependency(value = "net.megavex:scoreboard-library-modern:2.2.1"),
    RuntimeDependency(value = "net.megavex:scoreboard-library-implementation:2.2.1"),
    RuntimeDependency(value = "net.megavex:scoreboard-library-extra-kotlin:2.2.1"),
)
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
