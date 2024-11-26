package cn.wolfmc.minecraft.wolfhunter.infrastructure.persistence.entity

import cn.wolfmc.minecraft.wolfhunter.domain.model.base.Entity
import org.bukkit.configuration.serialization.ConfigurationSerializable
import java.util.UUID

abstract class StorageEntity : ConfigurationSerializable, Entity<UUID> 