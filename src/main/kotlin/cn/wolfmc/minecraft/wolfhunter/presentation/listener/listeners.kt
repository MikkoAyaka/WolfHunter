package cn.wolfmc.minecraft.wolfhunter.presentation.listener

import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.domain.component.plus
import cn.wolfmc.minecraft.wolfhunter.domain.event.StateChanged
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.reset
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.shouldResetPlayerOnJoin
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.updateGameMode
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.updateInvulnerable
import org.bukkit.event.player.PlayerJoinEvent

val gameModeUpdater =
    subscribe<PlayerJoinEvent> { it.player.updateGameMode() } +
        subscribe<StateChanged> { onlinePlayers().forEach { it.updateGameMode() } }

val protectionUpdater =
    subscribe<PlayerJoinEvent> { it.player.updateInvulnerable() } +
        subscribe<StateChanged> { onlinePlayers().forEach { it.updateInvulnerable() } }

val inventoryUpdater =
    subscribe<PlayerJoinEvent> { if (shouldResetPlayerOnJoin(it.player)) it.player.reset() }
