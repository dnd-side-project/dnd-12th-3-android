package com.dnd.safety.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Sample(
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: String,
): Parcelable {
    val displayName: String
        get() = "[$id] $name"
}