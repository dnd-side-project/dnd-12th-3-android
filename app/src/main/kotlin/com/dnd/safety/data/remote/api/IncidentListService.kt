package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.response.IncidentsResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IncidentListService {

    @GET("incidents/newest")
    suspend fun getIncidents(
        @Query("topRightX") topRightX: Double,
        @Query("topRightY") topRightY: Double,
        @Query("bottomLeftX") bottomLeftX: Double,
        @Query("bottomLeftY") bottomLeftY: Double,
        @Query("myX") myX: Double,
        @Query("myY") myY: Double,
    ): ApiResponse<IncidentsResponse>
}
