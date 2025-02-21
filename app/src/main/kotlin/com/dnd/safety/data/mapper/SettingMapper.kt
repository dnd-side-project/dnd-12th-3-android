package com.dnd.safety.data.mapper

import com.dnd.safety.data.local.entity.SettingEntity
import com.dnd.safety.domain.model.Setting

fun SettingEntity.toSetting() = Setting(
    isNotificationEnabled = isNotificationEnabled
)