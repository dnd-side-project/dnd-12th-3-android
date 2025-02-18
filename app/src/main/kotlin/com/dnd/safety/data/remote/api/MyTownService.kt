package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.request.MyTownRequest
import com.dnd.safety.data.model.response.DefaultResponse
import com.dnd.safety.data.model.response.MyTownResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MyTownService {

    @GET("member/address")
    suspend fun getMyTowns(): ApiResponse<MyTownResponse>

    @POST("member/address")
    suspend fun addMyTown(
        @Body request: MyTownRequest
    ): ApiResponse<DefaultResponse>

    @DELETE("member/address")
    suspend fun deleteMyTown(
        @Query("addressId") addressId: Long,
    ): ApiResponse<DefaultResponse>
}