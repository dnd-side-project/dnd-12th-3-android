package com.dnd.safety.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val placeName: String,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val latitude: Double,
    val longitude: Double,
)