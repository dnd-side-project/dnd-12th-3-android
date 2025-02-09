package com.dnd.safety.domain.datasource

import com.kakao.sdk.auth.model.OAuthToken
import com.skydoves.sandwich.ApiResponse

interface KakaoLoginDataSource {
    suspend fun login(): ApiResponse<OAuthToken>
}