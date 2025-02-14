package com.dnd.safety.domain.repository

import com.skydoves.sandwich.ApiResponse

interface AuthRepository {
    suspend fun loginWithKakao(): ApiResponse<String>
    suspend fun signInWithGoogle(idToken: String): ApiResponse<String>
}