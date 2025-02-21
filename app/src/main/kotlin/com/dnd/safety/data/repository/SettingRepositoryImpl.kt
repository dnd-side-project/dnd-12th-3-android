package com.dnd.safety.data.repository

import com.dnd.safety.data.local.dao.SettingDao
import com.dnd.safety.data.local.entity.SettingEntity
import com.dnd.safety.data.mapper.toSetting
import com.dnd.safety.domain.model.Setting
import com.dnd.safety.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val settingDao: SettingDao
) : SettingRepository {

    override suspend fun insertSetting(setting: Setting) {
        settingDao.insertSetting(SettingEntity(
            isNotificationEnabled = setting.isNotificationEnabled
        ))
    }

    override suspend fun getSetting(): Setting {
        return settingDao.getSetting().toSetting()
    }

    override fun getSettingFlow(): Flow<Setting> {
        return settingDao.getSettingFlow().map {
            it.toSetting()
        }
    }

    override suspend fun updateNotificationSetting(isNotificationEnabled: Boolean) {
        settingDao.updateNotificationSetting(isNotificationEnabled)
    }
}