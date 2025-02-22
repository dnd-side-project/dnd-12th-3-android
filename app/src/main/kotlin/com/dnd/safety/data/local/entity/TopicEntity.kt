package com.dnd.safety.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "topic")
data class TopicEntity(
    @PrimaryKey val id: Long = 0,
    val topic: String,
)
