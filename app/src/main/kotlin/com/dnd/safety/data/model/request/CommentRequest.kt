package com.dnd.safety.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CommentRequest(
    val content: String,
)
