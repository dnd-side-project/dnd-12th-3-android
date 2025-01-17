package com.dnd.safety.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SampleRequest(
    val id: Int,
    val name: String,
)
