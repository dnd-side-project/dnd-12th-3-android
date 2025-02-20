package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.response.DefaultResponse
import com.dnd.safety.data.model.response.IncidentsResponse
import com.dnd.safety.data.model.response.MyReportResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface MyReportService {

    @GET("incidents/writer/my")
    suspend fun getMyReport(
        @Query("key") cursor: Long?,
        @Query("size") size: Int = 30
    ): ApiResponse<MyReportResponse>

    @DELETE("incidnets")
    suspend fun deleteReport(
        @Query("incidentId") incidentId: Long,
    ): ApiResponse<DefaultResponse>
}