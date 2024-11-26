package cn.wolfmc.minecraft.wolfhunter.domain.repository.base

import cn.wolfmc.minecraft.wolfhunter.domain.model.base.Entity

interface Repository<T : Entity<ID>, ID> {
    fun save(entity: T)
    fun findById(id: ID): T?
    fun delete(id: ID)
    fun exists(id: ID): Boolean
} 