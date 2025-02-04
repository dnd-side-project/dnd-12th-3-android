package com.dnd.safety.data.model.response

import com.google.gson.annotations.SerializedName

data class GoogleSignInResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user_id") val userId: String
)