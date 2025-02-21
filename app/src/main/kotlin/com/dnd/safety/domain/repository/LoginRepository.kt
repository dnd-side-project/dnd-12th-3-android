package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.UserInfo
import com.skydoves.sandwich.ApiResponse

interface LoginRepository {

    suspend fun sendToken(
        name: String,
        email: String
    ): ApiResponse<UserInfo>

    suspend fun loginByKakao(token: String): ApiResponse<UserInfo>

    suspend fun loginByGoogle(token: String): ApiResponse<UserInfo>
}