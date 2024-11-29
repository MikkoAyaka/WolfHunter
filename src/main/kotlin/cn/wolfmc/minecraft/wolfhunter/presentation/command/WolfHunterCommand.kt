package cn.wolfmc.minecraft.wolfhunter.presentation.command

import cn.wolfmc.minecraft.wolfhunter.common.extensions.command
import cn.wolfmc.minecraft.wolfhunter.common.extensions.openMenu
import cn.wolfmc.minecraft.wolfhunter.presentation.item.ScaffoldBlock
import cn.wolfmc.minecraft.wolfhunter.presentation.menu.mainMenu
import cn.wolfmc.minecraft.wolfhunter.presentation.menu.testMenu
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

fun registerCommands(plugin: JavaPlugin) {
    plugin.command("wolfhunter") {
        argument("menu") { arg ->
            when (arg) {
                "main" -> openMenu(mainMenu)
                "test" -> openMenu(testMenu)
            }
        }
        literal("debug") {
            literal("toggle") {
                literal("invulnerable") {
                    runs {
                        if (this !is Player) return@runs
                        this.isInvulnerable = !this.isInvulnerable
                    }
                }
            }
            literal("inv") {
                runs {
                    if (this !is Player) return@runs
                    this.inventory.forEachIndexed { index, itemStack -> println("$index: $itemStack") }
                }
            }
            literal("scaffold") {
                runs {
                    if (this !is Player) return@runs
                    ScaffoldBlock.giveItem(this)
                }
            }
        }
    }
}
