package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.request.IncidentRequestDto
import com.dnd.safety.data.model.response.IncidentResponse
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface IncidentService {

    @Multipart
    @POST("incidents")
    suspend fun createIncident(
        @Part("incidentData") incidentData: IncidentRequestDto,
        @Part files: List<MultipartBody.Part>
    ): ApiResponse<Unit>

    @GET("incidents/{incidentId}")
    suspend fun getIncidentData(
        @Path("incidentId") incidentId: Long,
        @Query("myX") myX: Double,
        @Query("myY") myY: Double
    ): ApiResponse<IncidentResponse>

    @PATCH("incidents/{incidentId}")
    suspend fun updateIncident(
        @Part("incidentData") incidentData: IncidentRequestDto,
        @Part files: List<MultipartBody.Part>
    ): ApiResponse<Unit>

    @DELETE("incidents/{incidentId}")
    suspend fun deleteIncident(
        @Path("incidentId") incidentId: Long
    ): ApiResponse<Unit>
}