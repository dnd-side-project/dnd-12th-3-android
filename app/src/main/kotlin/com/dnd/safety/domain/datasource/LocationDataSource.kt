package com.dnd.safety.domain.datasource

import com.dnd.safety.data.model.KakaoAddressInfo
import com.skydoves.sandwich.ApiResponse

interface LocationDataSource {
    suspend fun getCurrentLocation(): ApiResponse<KakaoAddressInfo>
}