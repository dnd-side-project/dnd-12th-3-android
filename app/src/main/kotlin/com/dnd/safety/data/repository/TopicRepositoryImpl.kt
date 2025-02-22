package com.dnd.safety.data.repository

import com.dnd.safety.data.local.dao.TopicDao
import com.dnd.safety.data.local.entity.TopicEntity
import com.dnd.safety.domain.repository.TopicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TopicRepositoryImpl @Inject constructor(
    private val topicDao: TopicDao
) : TopicRepository {

    override suspend fun get(): List<TopicEntity> {
        return try {
            topicDao.getTopicFlow()
        } catch (e: Exception) {
            emptyList<TopicEntity>()
        }
    }

    override suspend fun insert(topicEntity: TopicEntity) {
        topicDao.insert(topicEntity)
    }

    override suspend fun delete(id: Long) {
        topicDao.delete(id)
    }
}