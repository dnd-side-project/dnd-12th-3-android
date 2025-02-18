package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String
)