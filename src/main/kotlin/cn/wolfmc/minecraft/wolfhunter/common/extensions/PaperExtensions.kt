package cn.wolfmc.minecraft.wolfhunter.common.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

/** 把 Component 转为传统的 &1Hello &2World 这样的字符串 */
fun Component.legacy() = LegacyComponentSerializer.legacySection().serialize(this)

/** 把 Component 转为不带颜色的纯文本 */
fun Component.plain() = PlainTextComponentSerializer.plainText().serialize(this)

/** 解析 <black>12345</black> 这样的字符串为带颜色的 Component */
fun String.miniMsg() = MiniMessage.miniMessage().deserialize(this)
