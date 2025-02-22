package com.dnd.safety.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dnd.safety.data.local.entity.FcmEntity
import com.dnd.safety.data.local.entity.SettingEntity
import com.dnd.safety.data.local.entity.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {

    @Query("SELECT * FROM topic")
    suspend fun getTopicFlow(): List<TopicEntity>

    @Insert
    suspend fun insert(topicEntity: TopicEntity)

    @Query("DELETE FROM topic WHERE id = :topickId")
    suspend fun delete(topickId: Long)
}