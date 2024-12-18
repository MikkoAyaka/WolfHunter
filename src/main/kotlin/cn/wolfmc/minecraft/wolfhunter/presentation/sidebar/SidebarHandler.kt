package cn.wolfmc.minecraft.wolfhunter.presentation.sidebar

import cn.wolfmc.minecraft.wolfhunter.application.uhc.UHCGameService
import cn.wolfmc.minecraft.wolfhunter.common.extensions.subscribe
import cn.wolfmc.minecraft.wolfhunter.common.extensions.unregister
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.event.Listener

object SidebarHandler : ScopeService {
    override fun init() {
    }

    private var sidebarService: ScopeService? = null
    private var listener: Listener? = null

    override fun enable() {
        listener =
            subscribe(GameEvent.ModeSelected::class) {
                if (it.gameService == UHCGameService) sidebarService = UHCSidebar
                sidebarService?.init()
                sidebarService?.enable()
            }
    }

    override fun disable() {
        listener?.unregister()
        sidebarService?.disable()
    }
}
