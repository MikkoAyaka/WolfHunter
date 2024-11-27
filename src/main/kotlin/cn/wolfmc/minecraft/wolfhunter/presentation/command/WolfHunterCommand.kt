package cn.wolfmc.minecraft.wolfhunter.presentation.command

import cn.wolfmc.minecraft.wolfhunter.common.extensions.command
import cn.wolfmc.minecraft.wolfhunter.common.extensions.openMenu
import cn.wolfmc.minecraft.wolfhunter.presentation.menu.mainMenu
import cn.wolfmc.minecraft.wolfhunter.presentation.menu.testMenu
import org.bukkit.plugin.java.JavaPlugin

fun registerCommands(plugin: JavaPlugin) {
    plugin.command("wolfhunter") {
        literal("test1") {
            runs { sendMessage("test1") }
        }
        literal("test2") {
            runs { sendMessage("test2") }
            literal("test3") {
                runs { sendMessage("test3") }
            }
        }
        argument<String>("test4") { arg ->
            runs { sendMessage("test4 arg: $arg") }
        }
        argument<String>("menu") { arg ->
            runs {
                when(arg) {
                    "main" -> openMenu(mainMenu)
                    "test" -> openMenu(testMenu)
                }
            }
        }
    }
}