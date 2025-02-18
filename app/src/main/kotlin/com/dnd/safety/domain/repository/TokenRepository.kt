package com.dnd.safety.domain.repository

interface TokenRepository {
    suspend fun getToken(): String
    suspend fun setToken(token: String)
}