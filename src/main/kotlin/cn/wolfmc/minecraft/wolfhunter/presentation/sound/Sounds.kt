package cn.wolfmc.minecraft.wolfhunter.presentation.sound

import org.bukkit.Sound

object Sounds {
    val DRAGON_GROWL = Sound.ENTITY_ENDER_DRAGON_GROWL.create(1f, 1f)
    val THUNDER = Sound.ENTITY_LIGHTNING_BOLT_THUNDER.create(1f, 1f)
    val WORLD_BREAK = Sound.ENTITY_ZOMBIE_VILLAGER_CURE.create(0.5f, 0.5f)
    val BELL = Sound.BLOCK_NOTE_BLOCK_BELL.create(0.8f, 1.2f)
    val PLING = Sound.BLOCK_NOTE_BLOCK_PLING.create(1f, 1f)
    val EXP_PICKUP = Sound.ENTITY_EXPERIENCE_ORB_PICKUP.create(0.1f, 2f)
    val ANVIL_USE = Sound.BLOCK_ANVIL_USE.create(0.1f, 2f)
}

fun Sound.create(
    volume: Float,
    pitch: Float,
) = net.kyori.adventure.sound.Sound
    .sound(this, net.kyori.adventure.sound.Sound.Source.MASTER, volume, pitch)
