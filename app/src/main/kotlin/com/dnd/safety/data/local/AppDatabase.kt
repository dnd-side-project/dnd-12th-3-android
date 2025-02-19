package com.dnd.safety.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dnd.safety.data.local.dao.SettingDao
import com.dnd.safety.data.local.entity.SettingEntity

@Database(
    entities = [SettingEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingDao(): SettingDao
}