package com.dnd.safety.domain.datasource

import com.skydoves.sandwich.ApiResponse

interface KakaoLoginDataSource {
    suspend fun login(): ApiResponse<String>
    suspend fun isUserLoggedIn(): Boolean
}