package com.dnd.safety.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "samples")
data class SampleEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val createdAt: String,
    val description: String,
    val isBookmarked: Boolean = false,
)
