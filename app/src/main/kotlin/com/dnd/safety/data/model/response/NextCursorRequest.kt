package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class NextCursorRequest(
    val key: Long,
    val size: Int
)