package com.dnd.safety.domain.usecase

import com.dnd.safety.domain.repository.AuthRepository
import com.kakao.sdk.common.model.ClientError
import javax.inject.Inject

class KakaoLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<String> {
        return try {
            authRepository.loginWithKakao().fold(
                onSuccess = { token ->
                    Result.success(token)
                },
                onFailure = { throwable ->
                    when (throwable) {
                        is ClientError -> Result.failure(Exception("카카오 로그인 취소"))
                        else -> Result.failure(Exception("로그인 실패"))
                    }
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}