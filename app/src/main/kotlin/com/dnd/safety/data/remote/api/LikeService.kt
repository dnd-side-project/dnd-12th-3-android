package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.response.DefaultResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeService {

    @POST("incidents/{incidentId}/likes")
    suspend fun toggleLike(
        @Path("incidentId") incidentId: Long,
    ): ApiResponse<DefaultResponse>

}