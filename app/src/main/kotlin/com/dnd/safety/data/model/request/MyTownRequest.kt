package com.dnd.safety.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class MyTownRequest(
    val addressName: String,
    val latitude: Double,
    val longitude: Double
)