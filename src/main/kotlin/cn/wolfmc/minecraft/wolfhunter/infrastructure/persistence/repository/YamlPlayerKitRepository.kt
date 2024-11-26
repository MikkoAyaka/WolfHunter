package cn.wolfmc.minecraft.wolfhunter.infrastructure.persistence.repository

import cn.wolfmc.minecraft.wolfhunter.domain.model.player.PlayerKit
import cn.wolfmc.minecraft.wolfhunter.domain.repository.PlayerKitRepository
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

class YamlPlayerKitRepository(
    private val plugin: JavaPlugin
) : PlayerKitRepository {
    override fun save(playerKit: PlayerKit) {
        // YAML 存储实现
    }

    override fun findByPlayer(player: Player): PlayerKit? {
        // YAML 查询实现
        return null
    }

    override fun delete(player: Player) {
        // YAML 删除实现
    }
} 