package com.dnd.safety.domain.usecase

import com.dnd.safety.domain.repository.AuthRepository
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<String> {
        return authRepository.signInWithGoogle(idToken)
    }
}