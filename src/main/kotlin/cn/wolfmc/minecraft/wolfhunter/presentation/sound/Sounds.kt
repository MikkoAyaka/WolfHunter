package cn.wolfmc.minecraft.wolfhunter.presentation.sound

import org.bukkit.Sound

object Sounds {
    val DRAGON_GROWL = Sound.ENTITY_ENDER_DRAGON_GROWL.create(1f, 1f)
    val THUNDER = Sound.ENTITY_LIGHTNING_BOLT_THUNDER.create(1f, 1f)
    val WORLD_BREAK = Sound.ENTITY_ZOMBIE_VILLAGER_CURE.create(0.7f, 0.5f)
}

fun Sound.create(
    volume: Float,
    pitch: Float,
) = net.kyori.adventure.sound.Sound
    .sound(this, net.kyori.adventure.sound.Sound.Source.MASTER, volume, pitch)
