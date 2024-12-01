package cn.wolfmc.minecraft.wolfhunter.application.uhc

import cn.wolfmc.minecraft.wolfhunter.common.extensions.legacy
import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import cn.wolfmc.minecraft.wolfhunter.common.extensions.onlinePlayers
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.Bukkit
import taboolib.common.platform.function.submit
import taboolib.platform.util.broadcast

object UHCEndingStage : ScopeService {
    override fun init() {}

    override fun enable() {
        val winTeam = GameInstance.allGameTeams().first { it.size() > 0 }
        "游戏结束，${winTeam.name} 取得了最终的胜利！".miniMsg().legacy().broadcast()
        "本局游戏将在 30 秒后清场重置，大约 1 分钟后可再次进入游玩。".miniMsg().legacy().broadcast()
        submit(delay = 20 * 30) {
            onlinePlayers().forEach { it.kick("游戏正在重置场地，稍后即可再次进入。".miniMsg()) }
            Bukkit.getServer().shutdown()
        }
    }

    override fun disable() {}
}
