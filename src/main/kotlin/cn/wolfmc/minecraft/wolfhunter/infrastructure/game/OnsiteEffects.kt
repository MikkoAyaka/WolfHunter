package cn.wolfmc.minecraft.wolfhunter.infrastructure.game

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.component.GameState
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.GameRule
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team

fun Player.isGamePlayer() = GameInstance.findGamePlayer(this) != null

fun World.setBorder(size: Double) {
    worldBorder.setCenter(0.0, 0.0)
    worldBorder.size = size
    worldBorder.damageAmount = 5.0
}

fun World.setRespawnRadius(radius: Int) {
    setSpawnLocation(0, getHighestBlockYAt(0, 0), 0)
    setGameRule(GameRule.SPAWN_RADIUS, radius)
}

fun initGameRules() {
    Bukkit.getWorlds().forEach { world -> world.setGameRule(GameRule.KEEP_INVENTORY, true) }
}

fun resetPlayer(player: Player) {
    player.exp = 0f
    player.level = 0
    player.inventory.clear()
}

fun Player.reset() = resetPlayer(this)

fun shouldResetPlayerOnJoin(player: Player): Boolean =
    when (GameInstance.state) {
        GameState.WAITING,
        GameState.STARTING,
        -> true
        GameState.RUNNING -> !player.isGamePlayer()
        GameState.ENDING -> true
    }

fun Player.updateGameMode() {
    gameMode =
        when (GameInstance.state) {
            GameState.WAITING,
            GameState.STARTING,
            -> GameMode.ADVENTURE
            GameState.RUNNING -> {
                if (isGamePlayer()) GameMode.SURVIVAL else GameMode.SPECTATOR
            }

            GameState.ENDING -> GameMode.CREATIVE
        }
}

fun Player.updateInvulnerable() {
    isInvulnerable =
        when (GameInstance.state) {
            GameState.WAITING,
            GameState.STARTING,
            -> true
            GameState.RUNNING,
            GameState.ENDING,
            -> false
        }
}

fun Player.isEnemy(another: Player): Boolean {
    // TODO
    return true
}

fun defaultScoreboardTeam(name: String) =
    Contexts.scoreboard.registerNewTeam(name).apply {
        this.color(NamedTextColor.WHITE) // 默认颜色
        this.setAllowFriendlyFire(false) // 禁止友伤
        this.setCanSeeFriendlyInvisibles(true) // 可以看到队友隐身
        this.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM) // 可以看到队友名牌
    }
