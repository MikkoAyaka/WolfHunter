package cn.wolfmc.minecraft.wolfhunter.common.extensions

import cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler.SpecialItemHandler
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun Player.gamePlayer() = GameInstance.findGamePlayer(this)

fun ItemStack.isSpecialItem(handler: SpecialItemHandler<*>) = handler.has(this)
