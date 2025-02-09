package com.dnd.safety.data.model

import com.dnd.safety.data.model.response.KakaoLocationResponse

data class KakaoAddressInfo(
    val latitude: Double,
    val longitude: Double,
    val addressResponse: KakaoLocationResponse
)