package com.dnd.safety.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
    val placeName: String,
    val address: String
)