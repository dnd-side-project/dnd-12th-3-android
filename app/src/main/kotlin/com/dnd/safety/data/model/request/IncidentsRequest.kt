package com.dnd.safety.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class IncidentsRequest(
    val pointTopRightX: Double,
    val pointTopRightY: Double,
    val pointBottomLeftX: Double,
    val pointBottomLeftY: Double
)
