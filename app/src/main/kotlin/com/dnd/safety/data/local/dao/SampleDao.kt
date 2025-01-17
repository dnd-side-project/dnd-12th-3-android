package com.dnd.safety.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dnd.safety.data.local.entity.SampleEntity

@Dao
interface SampleDao {
    @Query("SELECT * FROM samples")
    suspend fun getAll(): List<SampleEntity>
}