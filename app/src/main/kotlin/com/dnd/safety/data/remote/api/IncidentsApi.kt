package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.request.IncidentsRequest
import com.dnd.safety.data.model.response.IncidentsResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface IncidentsApi {

    @POST("incidents")
    suspend fun getIncidents(
        @Body request: IncidentsRequest
    ): ApiResponse<IncidentsResponse>
}
