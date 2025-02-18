package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.request.LoginRequest
import com.dnd.safety.data.model.response.LoginResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("oauth/kakao")
    suspend fun kakaoLogin(
        @Body request: LoginRequest
    ): ApiResponse<LoginResponse>

    @POST("oauth/google")
    suspend fun googleLogin(
        @Body request: LoginRequest
    ): ApiResponse<LoginResponse>
}