package com.dnd.safety.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dnd.safety.data.local.AppDatabase
import com.dnd.safety.data.local.converter.generateInsertQuery

@Entity(tableName = "setting")
data class SettingEntity(
    @PrimaryKey val id: Int = 0,
    val isNotificationEnabled: Boolean = false,
) {

    companion object {
        val DEFAULT_SETTING = SettingEntity(
            isNotificationEnabled = true
        )

        val INSERT_QUERY = generateInsertQuery(DEFAULT_SETTING, "setting")
    }
}
