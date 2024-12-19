package cn.wolfmc.minecraft.wolfhunter.presentation.ui

import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import java.time.Duration

fun showBattleStatusTitle(
    attacker: Player,
    defender: Player,
    damage: Double,
) {
    val health = defender.health
    val maxHealth = defender.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
    val text = "<white>%.1f<gray>(<red>%.1f</red>)</gray> <dark_gray>/</dark_gray> %.1f".format(health, -damage, maxHealth)
    val title =
        Title.title(
            Component.empty(),
            text.miniMsg(),
            Title.Times.times(Duration.ofMillis(50), Duration.ofMillis(400), Duration.ofMillis(50)),
        )
    attacker.showTitle(title)
}
