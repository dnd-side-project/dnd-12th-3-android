package com.dnd.safety.data.remote.datasource

import android.content.Context
import com.dnd.safety.domain.datasource.KakaoLoginDataSource
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.skydoves.sandwich.ApiResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KakaoLoginDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
): KakaoLoginDataSource {

    override suspend fun login(): ApiResponse<String> = suspendCoroutine { continuation ->
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            when {
                error != null -> continuation.resume(ApiResponse.Failure.Exception(error))
                token != null -> continuation.resume(ApiResponse.Success(token.accessToken))
                else -> continuation.resume(ApiResponse.Failure.Exception(Exception("알 수 없는 오류가 발생했습니다.")))
            }
        }

        try {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    when {
                        error != null -> {
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                continuation.resume(ApiResponse.Failure.Exception(error))
                                return@loginWithKakaoTalk
                            }
                            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                        }
                        token != null -> continuation.resume(ApiResponse.Success(token.accessToken))
                        else -> continuation.resume(ApiResponse.Failure.Exception(Exception("알 수 없는 오류가 발생했습니다.")))
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        } catch (e: Exception) {
            continuation.resume(ApiResponse.Failure.Exception(e))
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.me { user, error ->
                if (error == null && user != null) {
                    continuation.resume(true)
                } else {
                    continuation.resume(false)
                }
            }
        }
    }
}