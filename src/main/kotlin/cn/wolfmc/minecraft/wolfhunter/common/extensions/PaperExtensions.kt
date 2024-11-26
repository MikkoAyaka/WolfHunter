package cn.wolfmc.minecraft.wolfhunter.common.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

fun Component.legacy() = LegacyComponentSerializer.legacySection().serialize(this)
fun Component.plain() = PlainTextComponentSerializer.plainText().serialize(this)