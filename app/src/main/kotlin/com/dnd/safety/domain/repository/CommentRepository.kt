package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.Comments
import com.skydoves.sandwich.ApiResponse

interface CommentRepository {

    suspend fun getComments(
        incidentId: Long,
        cursor: Long,
    ): ApiResponse<Comments>

    suspend fun writeComment(
        incidentId: Long,
        comment: String,
    ): ApiResponse<Unit>

    suspend fun deleteComment(
        incidentId: Long,
        commentId: Long,
    ): ApiResponse<Unit>

    suspend fun editComment(
        incidentId: Long,
        commentId: Long,
        comment: String,
    ): ApiResponse<Unit>
}