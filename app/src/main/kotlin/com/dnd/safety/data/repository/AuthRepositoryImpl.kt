package com.dnd.safety.data.repository

import com.dnd.safety.data.model.request.GoogleLogInRequest
import com.dnd.safety.data.remote.api.GoogleAuthService
import com.dnd.safety.domain.datasource.KakaoLoginDataSource
import com.dnd.safety.domain.repository.AuthRepository
import com.dnd.safety.utils.Logger
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val kakaoLoginDataSource: KakaoLoginDataSource,
    private val googleAuthService: GoogleAuthService,
) : AuthRepository {

    override suspend fun loginWithKakao(): ApiResponse<String> {
        return kakaoLoginDataSource.login()
            .mapSuccess {
                accessToken
            }.onFailure {
                Logger.e(message())
            }
    }

    override suspend fun signInWithGoogle(idToken: String): ApiResponse<String> {
        return googleAuthService.googleSignIn(GoogleLogInRequest(idToken))
            .mapSuccess {
                accessToken
            }.onFailure {
                Logger.e(message())
            }
    }
}