package cn.wolfmc.minecraft.wolfhunter.common.extensions

import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import org.bukkit.entity.Player

fun Player.gamePlayer() = GameInstance.gamePlayers[this.uniqueId]
