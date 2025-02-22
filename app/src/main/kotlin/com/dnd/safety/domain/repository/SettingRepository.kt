package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.Setting
import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    suspend fun insertSetting(setting: Setting)

    suspend fun getSetting(): Setting?

    fun getSettingFlow(): Flow<Setting>

    suspend fun updateNotificationSetting(isNotificationEnabled: Boolean)
}