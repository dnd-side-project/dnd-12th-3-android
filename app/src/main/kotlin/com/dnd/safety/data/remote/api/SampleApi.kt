package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.request.SampleRequest
import com.dnd.safety.data.model.response.SampleResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SampleApi {
    @GET("samples")
    suspend fun getSamples(): ApiResponse<List<SampleResponse>>

    @GET("samples/{id}")
    suspend fun getSampleById(
        @Path("id") id: Int
    ): ApiResponse<SampleResponse>

    @POST("samples")
    suspend fun createSample(
        @Body request: SampleRequest
    ): ApiResponse<SampleResponse>
}
