package cn.wolfmc.minecraft.wolfhunter.common.exception

import kotlin.reflect.KClass

sealed class WolfHunterException(message: String) : RuntimeException(message)

class ServiceNotFoundException(serviceClass: KClass<*>) :
    WolfHunterException("Service not found: ${serviceClass.simpleName}") 