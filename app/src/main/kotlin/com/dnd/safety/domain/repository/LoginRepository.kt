package com.dnd.safety.domain.repository

import com.skydoves.sandwich.ApiResponse

interface LoginRepository {

    suspend fun loginByKakao(token: String): ApiResponse<String>

    suspend fun loginByGoogle(token: String): ApiResponse<String>
}