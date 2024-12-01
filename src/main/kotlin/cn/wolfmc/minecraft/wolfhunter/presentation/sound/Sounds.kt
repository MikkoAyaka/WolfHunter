package cn.wolfmc.minecraft.wolfhunter.presentation.sound

import org.bukkit.Sound

object Sounds {
    val DRAGON_GROWL = Sound.ENTITY_ENDER_DRAGON_GROWL.create(1f, 1f)
    val THUNDER = Sound.ENTITY_LIGHTNING_BOLT_THUNDER.create(1f, 1f)
}

fun Sound.create(
    volume: Float,
    pitch: Float,
) = net.kyori.adventure.sound.Sound
    .sound(this, net.kyori.adventure.sound.Sound.Source.MASTER, volume, pitch)
