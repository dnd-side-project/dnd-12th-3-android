package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.response.IncidentsResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IncidentsApi {

    @GET("/api/incidents/nearby")
    suspend fun getIncidents(
        @Query("pointX") latitude: Double,
        @Query("pointY") longitude: Double,
        @Query("radiusInKm") distance: Int = 500
    ): ApiResponse<IncidentsResponse>
}
