package com.dnd.safety.domain.repository

import com.skydoves.sandwich.ApiResponse

interface AuthRepository {

    suspend fun checkKakaoLogin(): Boolean

    suspend fun loginWithKakao(): ApiResponse<String>
}