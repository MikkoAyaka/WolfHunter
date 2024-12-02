package cn.wolfmc.minecraft.wolfhunter.presentation.command

import cn.wolfmc.minecraft.wolfhunter.common.extensions.giveItemSafely
import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.AutomaticGameStarter
import cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler.ScaffoldBlockHandler
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.component.GameState
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent.CountdownFinished
import cn.wolfmc.minecraft.wolfhunter.presentation.menu.MainMenu
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.module.ui.openMenu
import taboolib.platform.util.buildItem

@CommandHeader("wolfhunter", ["wh", "haw", "hw"])
object WolfHunterCommand {
    @CommandBody
    val menu =
        subCommand {
            dynamic {
                suggest { listOf("main") }
                execute<Player> { sender, _, arg ->
                    when (arg) {
                        "main" -> sender.openMenu(MainMenu.inventory)
                    }
                }
            }
        }

    @CommandBody
    val debug =
        subCommand {
            literal("toggle-invulnerable") {
                execute<Player> { sender, _, _ ->
                    sender.isInvulnerable = !sender.isInvulnerable
                }
            }
            literal("inv") {
                execute<Player> { sender, _, _ ->
                    sender.inventory.forEachIndexed { index, itemStack -> println("$index: $itemStack") }
                }
            }
            literal("scaffold-block") {
                execute<Player> { sender, _, _ ->
                    ScaffoldBlockHandler.giveItem(sender)
                }
            }
            literal("game-start") {
                execute<Player> { sender, _, _ ->
                    if (GameInstance.state == GameState.WAITING) {
                        CountdownFinished(AutomaticGameStarter).callEvent()
                        sender.sendMessage("已触发开始游戏")
                    } else {
                        sender.sendMessage("未在准备阶段")
                    }
                }
            }
            literal("item-debug") {
                execute<Player> { sender, context, argument ->
                    sender.giveItemSafely(testItem)
                    subscribe(PlayerInteractEvent::class) {
                        println("${it.item == testItem}")
                    }
                }
            }
        }
}

val testItem =
    buildItem(Material.DIAMOND_ORE) {
        this.name = "abab"
        this.lore.add("114514")
    }
