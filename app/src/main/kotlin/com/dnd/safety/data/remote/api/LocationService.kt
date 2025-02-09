package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.response.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {
    @GET("v2/local/search/keyword.json")
    suspend fun searchToQueryLocation(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): LocationResponse
}