package com.dnd.safety.domain.repository

interface AuthRepository {
    suspend fun loginWithKakao(): Result<String>
}