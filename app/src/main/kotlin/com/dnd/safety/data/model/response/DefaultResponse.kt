package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class DefaultResponse(
    val code: String = "",
    val data: String? = null,
    val timestamp: String = ""
)