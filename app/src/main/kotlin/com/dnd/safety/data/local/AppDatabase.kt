package com.dnd.safety.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dnd.safety.data.local.dao.SampleDao
import com.dnd.safety.data.local.entity.SampleEntity

@Database(
    entities = [SampleEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sampleDao(): SampleDao
}