package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.response.DefaultResponse
import com.dnd.safety.data.model.response.IncidentsResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface MyReportService {

    @GET(BASE_URL)
    suspend fun getMyReport(): ApiResponse<IncidentsResponse>

    @DELETE(BASE_URL)
    suspend fun deleteReport(
        @Query("incidentId") incidentId: Long,
    ): ApiResponse<DefaultResponse>

    companion object {
        private const val BASE_URL = "incidnets/my"
    }
}