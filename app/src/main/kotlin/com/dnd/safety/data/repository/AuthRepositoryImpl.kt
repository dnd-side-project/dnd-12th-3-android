package com.dnd.safety.data.repository

import com.dnd.safety.data.model.request.GoogleLogInRequest
import com.dnd.safety.data.remote.api.GoogleAuthService
import com.dnd.safety.domain.datasource.KakaoLoginDataSource
import com.dnd.safety.domain.repository.AuthRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val kakaoLoginDataSource: KakaoLoginDataSource,
    private val googleAuthService: GoogleAuthService,
) : AuthRepository {
    override suspend fun loginWithKakao(): Result<String> {
        return when (val response = kakaoLoginDataSource.login()) {
            is ApiResponse.Success -> Result.success(response.data.accessToken)
            is ApiResponse.Failure -> {
                when (response) {
                    is ApiResponse.Failure.Error -> Result.failure(Exception(response.message()))
                    is ApiResponse.Failure.Exception -> Result.failure(response.throwable)
                }
            }
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<String> {
        return when (val response = googleAuthService.googleSignIn(GoogleLogInRequest(idToken))) {
            is ApiResponse.Success -> {
                Result.success(response.data.accessToken)
            }
            is ApiResponse.Failure -> {
                when (response) {
                    is ApiResponse.Failure.Error -> Result.failure(Exception(response.message()))
                    is ApiResponse.Failure.Exception -> Result.failure(response.throwable)
                }
            }
        }
    }
}