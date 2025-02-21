package com.dnd.safety.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dnd.safety.data.local.entity.SettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {

    @Insert
    suspend fun insertSetting(settingEntity: SettingEntity)

    @Query("SELECT * FROM setting")
    suspend fun getSetting(): SettingEntity

    @Query("SELECT * FROM setting")
    fun getSettingFlow(): Flow<SettingEntity>

    @Query("UPDATE setting SET isNotificationEnabled = :isNotificationEnabled")
    suspend fun updateNotificationSetting(isNotificationEnabled: Boolean)
}