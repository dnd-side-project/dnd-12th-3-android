package com.dnd.safety.data.repository

import com.dnd.safety.data.remote.api.LikeService
import com.dnd.safety.domain.repository.LikeRepository
import com.dnd.safety.utils.Logger
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val likeService: LikeService
) : LikeRepository {

    override suspend fun toggleLike(incidentId: Long) {
        likeService
            .toggleLike(incidentId)
            .onFailure {
                Logger.d(message())
            }
    }
}