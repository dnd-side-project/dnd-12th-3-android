package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val data: Data
) {

    @Serializable
    data class Data(
        val accessToken: String,
        val name: String = ""
    )
}