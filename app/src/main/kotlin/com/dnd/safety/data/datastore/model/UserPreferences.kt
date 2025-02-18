package com.dnd.safety.data.datastore.model

import com.dnd.safety.utils.Const.DEFAULT_TOKEN
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val token: String = DEFAULT_TOKEN
)