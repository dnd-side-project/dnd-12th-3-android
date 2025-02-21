package com.dnd.safety.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val token: String
)

@Serializable
data class TokenRequest(
    val name: String,
    val email: String
)