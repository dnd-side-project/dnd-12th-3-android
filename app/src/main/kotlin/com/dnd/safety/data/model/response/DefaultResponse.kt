package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class DefaultResponse(
    val code: String = "",
    val data: JsonElement? = null,
    val timestamp: String = ""
)