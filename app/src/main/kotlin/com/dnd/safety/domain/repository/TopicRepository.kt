package com.dnd.safety.domain.repository

import com.dnd.safety.data.local.entity.TopicEntity
import kotlinx.coroutines.flow.Flow

interface TopicRepository {

    suspend fun get(): List<TopicEntity>

    suspend fun insert(topicEntity: TopicEntity)

    suspend fun delete(id: Long)
}