package com.dnd.safety.domain.repository

interface AuthRepository {
    suspend fun loginWithKakao(): Result<String>
        suspend fun signInWithGoogle(idToken: String): Result<String>
}