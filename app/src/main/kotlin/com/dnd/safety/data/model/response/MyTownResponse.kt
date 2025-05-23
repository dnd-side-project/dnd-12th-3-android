package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MyTownResponse(
    val code: String = "",
    val message: String = "",
    val data: List<Data>
) {

    @Serializable
    data class Data(
        val addressId: Long,
        val addressName: String?,
        val latitude: Double,
        val longitude: Double
    )
}
