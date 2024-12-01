package cn.wolfmc.minecraft.wolfhunter.presentation.command

import cn.wolfmc.minecraft.wolfhunter.presentation.item.ScaffoldBlock
import cn.wolfmc.minecraft.wolfhunter.presentation.menu.MainMenu
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.module.ui.openMenu

@CommandHeader("wolfhunter", ["wh", "haw", "hw"])
object WolfHunterCommand {
    @CommandBody
    val menu =
        subCommand {
            dynamic {
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
                    ScaffoldBlock.giveItem(sender)
                }
            }
        }
}
