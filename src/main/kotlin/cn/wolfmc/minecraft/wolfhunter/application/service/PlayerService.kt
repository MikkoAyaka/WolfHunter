package cn.wolfmc.minecraft.wolfhunter.application.service

import cn.wolfmc.minecraft.wolfhunter.application.dto.PlayerDTO
import cn.wolfmc.minecraft.wolfhunter.domain.service.ApplicationService
import org.bukkit.entity.Player

class PlayerService : ApplicationService {
    fun getPlayerData(player: Player): PlayerDTO {
        TODO()
    }
    
    override fun initialize() {
        // 初始化玩家服务
    }

    override fun shutdown() {
        // 关闭玩家服务
    }
} 