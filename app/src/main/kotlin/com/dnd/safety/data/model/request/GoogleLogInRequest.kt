package com.dnd.safety.data.model.request

import com.google.gson.annotations.SerializedName

data class GoogleLogInRequest(
    @SerializedName("id_token") val idToken: String
)