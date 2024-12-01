package cn.wolfmc.minecraft.wolfhunter.model.service

interface ScopeService {
    /** 自始至终都只应该被运行一次 */
    fun init()

    fun enable()

    fun disable()
}
