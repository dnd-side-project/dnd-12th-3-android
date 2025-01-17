package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SampleResponse(
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: String,
)