package com.dnd.safety.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "setting")
data class SettingEntity(
    @PrimaryKey val id: Int = 0,
    val isNotificationEnabled: Boolean = false,
)
