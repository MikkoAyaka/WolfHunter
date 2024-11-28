package cn.wolfmc.minecraft.wolfhunter.common.extensions

import cn.wolfmc.minecraft.wolfhunter.presentation.i18n.I18n
import java.util.logging.Level
import java.util.logging.Logger
import net.kyori.adventure.text.Component

fun Logger.log(level: Level, msg: Component) {
    this.log(level, msg.legacy())
}

fun Logger.logT(level: Level, key: String) {
    this.log(level, I18n.t(key).legacy())
}
