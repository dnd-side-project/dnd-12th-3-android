package com.dnd.safety.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dnd.safety.data.local.entity.FcmEntity
import com.dnd.safety.data.local.entity.SettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FcmDao {

    @Query("SELECT * FROM fcmTable")
    fun getFcmFlow(): Flow<FcmEntity>

    @Insert
    suspend fun insertFcm(fcmEntity: FcmEntity)

    @Query("DELETE FROM fcmTable WHERE id = :fcmId")
    suspend fun deleteFcm(fcmId: Long)
}