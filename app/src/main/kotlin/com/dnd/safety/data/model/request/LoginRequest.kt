package com.dnd.safety.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val token: String
)