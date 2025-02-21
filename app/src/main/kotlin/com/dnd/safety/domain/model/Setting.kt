package com.dnd.safety.domain.model

import com.dnd.safety.data.local.entity.SettingEntity

data class Setting(
    val isNotificationEnabled: Boolean = false
)