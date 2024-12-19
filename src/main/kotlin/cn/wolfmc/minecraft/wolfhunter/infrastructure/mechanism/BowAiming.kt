package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.constants.isRemoteWeapon
import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import cn.wolfmc.minecraft.wolfhunter.infrastructure.game.isEnemy
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import kotlinx.coroutines.delay
import org.bukkit.GameMode
import org.bukkit.entity.Player
import taboolib.common.platform.function.runTask
import taboolib.expansion.chain

object BowAiming : ScopeService {
    override fun init() {
    }

    private var job: TBJob? = null

    override fun enable() {
        job =
            chain {
                while (true) {
                    delay(1000)
                    survivalPlayers().forEach {
                        if (it.itemInUse?.type?.isRemoteWeapon() != true) return@forEach
                        if (it.handRaisedTime < 20 * 3) return@forEach
                        launch { autoAiming(it) }
                    }
                }
            }
    }

    override fun disable() {
        job?.cancel()
    }

    private suspend fun autoAiming(player: Player) {
        // 最近可以看到的敌人
        val nearestVisibleEnemy =
            player
                .nearbyPlayers(50)
                .filter { it.gameMode == GameMode.SURVIVAL && it.isEnemy(player) }
                .sortedBy { it.location.distance(player.location) }
                .firstOrNull { player.hasLineOfSight(it) }
                ?: return

        val deltaYaw = player.targetYaw(nearestVisibleEnemy.location)
        if (deltaYaw in -1.5..1.5) return
        val repeats = 20
        repeat(repeats) {
            delay(800L / repeats)
            runTask { player.teleport(player.location.apply { yaw += deltaYaw / repeats.toFloat() }) }
        }
    }
}
