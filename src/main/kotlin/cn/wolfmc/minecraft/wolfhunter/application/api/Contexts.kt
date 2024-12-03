package cn.wolfmc.minecraft.wolfhunter.application.api

import cn.wolfmc.minecraft.wolfhunter.model.service.GameService
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary
import org.bukkit.Bukkit
import org.bukkit.World
import taboolib.platform.BukkitPlugin

object Contexts {
    val plugin by lazy { BukkitPlugin.getInstance() }
    lateinit var gameService: GameService
    val logger by lazy { plugin.logger }
    val scoreboardLib by lazy { ScoreboardLibrary.loadScoreboardLibrary(plugin) }
    val scoreboardTeamManager by lazy { scoreboardLib.createTeamManager() }
    val worldMain: World by lazy { Bukkit.getWorlds().first { it.environment == World.Environment.NORMAL } }
    val worldNether: World by lazy { Bukkit.getWorlds().first { it.environment == World.Environment.NETHER } }
    val worldTheEnd: World by lazy { Bukkit.getWorlds().first { it.environment == World.Environment.THE_END } }
}
