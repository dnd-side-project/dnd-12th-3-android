package com.dnd.safety.data.repository

import com.dnd.safety.data.local.dao.SettingDao
import com.dnd.safety.data.local.entity.SettingEntity
import com.dnd.safety.data.mapper.toSetting
import com.dnd.safety.domain.model.Setting
import com.dnd.safety.domain.repository.SettingRepository
import com.dnd.safety.domain.repository.TopicRepository
import com.dnd.safety.utils.Logger
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val settingDao: SettingDao,
    private val topicRepository: TopicRepository
) : SettingRepository {

    override suspend fun insertSetting(setting: Setting) {
        subscribeTopic(setting.isNotificationEnabled)

        settingDao.insertSetting(
            SettingEntity(
                isNotificationEnabled = setting.isNotificationEnabled
            )
        )
    }

    override suspend fun getSetting(): Setting? {
        return settingDao.getSetting()?.toSetting()
    }

    override fun getSettingFlow(): Flow<Setting> {
        val settingDao = try {
            settingDao.getSettingFlow().map { it.toSetting() }
        } catch (e: Exception) {
            Logger.e("getSettingFlow error: $e")
            CoroutineScope(Dispatchers.IO).launch {
                insertSetting(Setting(isNotificationEnabled = true))
            }
            return getSettingFlow()
        }

        return settingDao
    }

    override suspend fun updateNotificationSetting(isNotificationEnabled: Boolean) {
        subscribeTopic(isNotificationEnabled)
        settingDao.updateNotificationSetting(isNotificationEnabled)
    }

    private suspend fun subscribeTopic(isNotificationEnabled: Boolean) {
        if (isNotificationEnabled) {
            topicRepository.get().forEach {
                FirebaseMessaging.getInstance().subscribeToTopic(it.topic)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Logger.d("subscribeTopic success ${it.topic}")
                        } else {
                            Logger.e("subscribeTopic failed")
                        }
                    }
            }
        } else {
            topicRepository.get().forEach {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(it.topic)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Logger.d("unsubscribeFromTopic success ${it.topic}")
                        } else {
                            Logger.e("unsubscribeFromTopic failed")
                        }
                    }
            }
        }
    }
}