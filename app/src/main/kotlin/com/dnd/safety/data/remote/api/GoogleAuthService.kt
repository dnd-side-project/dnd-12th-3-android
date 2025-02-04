package com.dnd.safety.data.remote.api

import com.dnd.safety.data.model.request.GoogleLogInRequest
import com.dnd.safety.data.model.response.AuthResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GoogleAuthService {
    @POST("api/auth/google")
    suspend fun googleSignIn(
        @Body request: GoogleLogInRequest
    ): ApiResponse<AuthResponse>
}