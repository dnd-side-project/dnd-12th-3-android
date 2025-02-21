package com.dnd.safety.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dnd.safety.data.local.converter.LocalDateTimeConverter
import com.dnd.safety.data.local.dao.FcmDao
import com.dnd.safety.data.local.dao.SettingDao
import com.dnd.safety.data.local.entity.FcmEntity
import com.dnd.safety.data.local.entity.SettingEntity

@Database(
    entities = [
        SettingEntity::class,
        FcmEntity::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    value = [
        LocalDateTimeConverter::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingDao(): SettingDao
    abstract fun fcmDao(): FcmDao
}