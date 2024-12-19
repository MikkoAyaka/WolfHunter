package cn.wolfmc.minecraft.wolfhunter.model.service

import cn.wolfmc.minecraft.wolfhunter.model.component.EventHandlerSet
import cn.wolfmc.minecraft.wolfhunter.model.component.GameInstance
import java.util.*

abstract class GameService : ScopeService {
    val eventHandlerSet = EventHandlerSet()
    protected var currentGame: GameInstance = GameInstance
    protected var currentStateService: ScopeService? = null
    protected val mechanism: MutableSet<ScopeService> = mutableSetOf()
}
