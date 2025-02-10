package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.request.IncidentRequestDto
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IncidentService {
    @Multipart
    @POST("api/incidents")
    suspend fun createIncident(
        @Part("incidentData") incidentData: IncidentRequestDto,
        @Part files: List<MultipartBody.Part>
    ): Response<ApiResponse<Unit>>
}