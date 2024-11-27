package cn.wolfmc.minecraft.wolfhunter.application.service

import cn.wolfmc.minecraft.wolfhunter.common.extensions.MenuListener
import cn.wolfmc.minecraft.wolfhunter.domain.service.ApplicationService
import cn.wolfmc.minecraft.wolfhunter.domain.component.ListenerGroup

object GlobalService: ApplicationService {

    private val listenerGroup = ListenerGroup(MenuListener)

    override fun init() {
    }

    override fun enable() {
        listenerGroup.registerAll()
    }

    override fun disable() {
        listenerGroup.unregisterAll()
    }

}