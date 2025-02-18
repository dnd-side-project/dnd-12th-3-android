package com.dnd.safety.data.repository

import com.dnd.safety.data.mapper.toUserInfo
import com.dnd.safety.data.model.request.LoginRequest
import com.dnd.safety.data.remote.api.LoginService
import com.dnd.safety.domain.model.UserInfo
import com.dnd.safety.domain.repository.LoginRepository
import com.dnd.safety.utils.Logger
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService
) : LoginRepository {

    override suspend fun loginByKakao(token: String): ApiResponse<UserInfo> {
        return loginService
            .kakaoLogin(LoginRequest(token))
            .mapSuccess {
                toUserInfo()
            }
            .onFailure {
                Logger.e(message())
            }.onError {
                Logger.e(message())
            }
    }

    override suspend fun loginByGoogle(token: String): ApiResponse<UserInfo> {
        return loginService
            .googleLogin(LoginRequest(token))
            .mapSuccess {
                toUserInfo()
            }
            .onFailure {
                Logger.e(message())
            }.onError {
                Logger.e(message())
            }
    }
}