package com.dnd.safety.domain.repository

interface UserInfoRepository {
    suspend fun setUserInfo(token: String, name: String)

    suspend fun getToken(): String
    suspend fun getName(): String
}