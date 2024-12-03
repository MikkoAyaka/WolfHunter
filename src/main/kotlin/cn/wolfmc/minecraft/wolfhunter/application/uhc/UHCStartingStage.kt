package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.ReadyCounter
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.data.GameTeam
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent.CountdownFinished
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.bossbar.gameStarterBossBar
import net.kyori.adventure.text.format.NamedTextColor
import net.megavex.scoreboardlibrary.extra.kotlin.playerColor
import kotlin.math.min

object UHCStartingStage : ScopeService {
    private val readyCounter = ReadyCounter

    override fun init() {
        readyCounter.init()
    }

    private val bar = gameStarterBossBar(readyCounter, 15)

    override fun enable() {
        // 启用进度条
        bar.init()
        // 队伍数量规划
        val teamAmount = if (onlinePlayers().size >= 2) scheduleTeamAmount() else 1
        // 初始化队伍与玩家
        initTeams(teamAmount)
        val teams = GameInstance.allGameTeams().toList()
        // 初始化队伍颜色
        initTeamColors(teams)
        // 队伍成员传送到一起
        teams.forEach { team ->
            val players = team.get()
            val loc = players.mapNotNull { it.player }.randomOrNull()?.location ?: return@forEach
            players.forEach { it.player?.teleportAsync(loc) }
        }
        // 计时开始
        readyCounter.enable()
        // 监听计时结束
        subscribe(CountdownFinished::class) {
            if (it.counter is ReadyCounter) {
                readyCounter.disable()
                GameInstance.nextState()
                unregister()
            }
        }
    }

    override fun disable() {
    }

    private val teamScheduleMap =
        mapOf(
            2 to listOf(2),
            3 to listOf(3),
            4 to listOf(2, 4),
            5 to listOf(2, 5),
            6 to listOf(2, 3, 6),
            7 to listOf(2, 3, 7),
            8 to listOf(2, 4, 8),
            9 to listOf(2, 3, 9),
            10 to listOf(2, 3, 5, 10),
            11 to listOf(2, 3, 4, 5, 11),
            12 to listOf(2, 3, 4, 6, 12),
        )

    // 根据玩家人数自动规划队伍数
    private fun scheduleTeamAmount(): Int {
        val players = onlinePlayers().size
        if (players < 2) throw IllegalStateException("Require 2 players to start at least")
        val teamChoices = teamScheduleMap[min(players, 12)]!!
        var choice = teamChoices.random()
        var maxRetry = 2
        // 只分两个队伍，或者每个队伍平均人数小于1时，最多可以重随机选择2次
        while ((players / choice.toDouble() < 1.0 || choice == 2) && maxRetry-- > 0) {
            choice = teamChoices.random()
        }
        return choice
    }

    // 初始化队伍并绑定玩家
    private fun initTeams(teams: Int) {
        val players = onlinePlayers()
        val playerSize = players.size
        if (teams <= 0) throw IllegalArgumentException("队伍数必须大于0")
        if (playerSize < teams) throw IllegalArgumentException("玩家数不能少于队伍数")

        // 计算每个队伍的基础人数和多余人数
        val baseSize = playerSize / teams
        val extra = playerSize % teams

        // 随机打乱玩家列表
        val shuffledPlayers = players.shuffled()

        // 创建队伍列表
        val gameTeams = List(teams) { GameTeam("队伍 $it") }

        // 分配玩家到队伍
        var playerIndex = 0
        for (teamIndex in gameTeams.indices) {
            // 当前队伍人数：基础人数 + 1（如果在前 extra 个队伍中）
            val currentTeamSize = baseSize + if (teamIndex < extra) 1 else 0
            for (i in 0 until currentTeamSize) {
                val player = shuffledPlayers[playerIndex++]
                GameInstance.join(player, gameTeams[teamIndex]) // 绑定玩家到队伍
            }
        }
    }

    private fun initTeamColors(teams: List<GameTeam>) {
        teams.forEachIndexed { index, gameTeam ->
            val color: NamedTextColor =
                when (index % 12) {
                    0 -> NamedTextColor.RED
                    1 -> NamedTextColor.GOLD
                    2 -> NamedTextColor.YELLOW
                    3 -> NamedTextColor.GREEN
                    4 -> NamedTextColor.AQUA
                    5 -> NamedTextColor.BLUE
                    6 -> NamedTextColor.DARK_BLUE
                    7 -> NamedTextColor.LIGHT_PURPLE
                    8 -> NamedTextColor.DARK_RED
                    9 -> NamedTextColor.DARK_AQUA
                    10 -> NamedTextColor.DARK_GREEN
                    11 -> NamedTextColor.DARK_PURPLE
                    else -> throw IllegalArgumentException("Illegal argument: index -> $index")
                }
            gameTeam.scoreboardTeam.playerColor(color)
        }
    }
}
