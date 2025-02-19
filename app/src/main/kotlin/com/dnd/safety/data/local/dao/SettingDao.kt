package com.dnd.safety.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dnd.safety.data.local.entity.SettingEntity

@Dao
interface SettingDao {

    @Query("SELECT * FROM setting")
    suspend fun getSetting(): SettingEntity

    @Query("UPDATE setting SET isNotificationEnabled = :isNotificationEnabled")
    suspend fun updateNotificationSetting(isNotificationEnabled: Boolean)
}