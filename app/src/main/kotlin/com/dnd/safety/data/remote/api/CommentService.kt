package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.request.CommentRequest
import com.dnd.safety.data.model.response.CommentResponse
import com.dnd.safety.data.model.response.DefaultResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentService {

    @GET("$BASE_URL/cursor")
    suspend fun getComments(
        @Path("incidentId") incidentId: Long,
        @Query("key") cursor: Long?,
        @Query("size") size: Int = 30,
    ): ApiResponse<CommentResponse>

    @POST(BASE_URL)
    suspend fun writeComment(
        @Path("incidentId") incidentId: Long,
        @Body request: CommentRequest
    ): ApiResponse<DefaultResponse>

    @DELETE("$BASE_URL/{commentId}")
    suspend fun deleteComment(
        @Path("incidentId") incidentId: Long,
        @Path("commentId") commentId: Long,
    ): ApiResponse<DefaultResponse>

    @PUT("$BASE_URL/{commentId}")
    suspend fun editComment(
        @Path("incidentId") incidentId: Long,
        @Path("commentId") commentId: Long,
        @Body request: CommentRequest
    ): ApiResponse<CommentResponse>

    companion object {
        private const val BASE_URL = "incidents/{incidentId}/comments"
    }
}