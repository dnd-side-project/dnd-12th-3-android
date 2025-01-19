package com.dnd.safety.data.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val token: String = DEFAULT_TOKEN
) {

    companion object {
        const val DEFAULT_TOKEN = ""
    }
}
