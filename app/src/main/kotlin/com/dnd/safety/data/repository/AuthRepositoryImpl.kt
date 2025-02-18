package com.dnd.safety.data.repository

import com.dnd.safety.domain.datasource.KakaoLoginDataSource
import com.dnd.safety.domain.repository.AuthRepository
import com.dnd.safety.utils.Logger
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val kakaoLoginDataSource: KakaoLoginDataSource,
) : AuthRepository {

    override suspend fun checkKakaoLogin(): Boolean {
        return kakaoLoginDataSource.isUserLoggedIn()
    }

    override suspend fun loginWithKakao(): ApiResponse<String> {
        return kakaoLoginDataSource.login()
            .onFailure {
                Logger.e(message())
            }.onError {
                Logger.e(message())
            }
    }
}