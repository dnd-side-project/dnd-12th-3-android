package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class DefaultResponse(
    val code: String = "",
    val data: String,
    val timestamp: String
)