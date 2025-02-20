package com.dnd.safety.domain.repository

interface LikeRepository {

    suspend fun toggleLike(incidentId: Long)
}