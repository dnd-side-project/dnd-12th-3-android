package com.dnd.safety.data.repository

import com.dnd.safety.data.mapper.toComments
import com.dnd.safety.data.model.request.CommentRequest
import com.dnd.safety.data.remote.api.CommentService
import com.dnd.safety.domain.model.Comments
import com.dnd.safety.domain.repository.CommentRepository
import com.dnd.safety.utils.Logger
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentService: CommentService
) : CommentRepository {

    override suspend fun getComments(
        incidentId: Long,
        cursor: Long
    ): ApiResponse<Comments> {
        return commentService
            .getComments(incidentId, cursor)
            .mapSuccess { toComments() }
            .onFailure {
                Logger.e(message())
            }
    }

    override suspend fun writeComment(incidentId: Long, comment: String): ApiResponse<Unit> {
        return commentService
            .writeComment(incidentId, CommentRequest(comment))
            .mapSuccess {  }
            .onFailure {
                Logger.e(message())
            }
    }

    override suspend fun deleteComment(incidentId: Long, commentId: Long): ApiResponse<Unit> {
        return commentService
            .deleteComment(incidentId, commentId)
            .mapSuccess {  }
            .onFailure {
                Logger.e(message())
            }
    }

    override suspend fun editComment(incidentId: Long, commentId: Long, comment: String): ApiResponse<Unit> {
        return commentService
            .editComment(incidentId, commentId, comment)
            .mapSuccess {  }
            .onFailure {
                Logger.e(message())
            }
    }
}