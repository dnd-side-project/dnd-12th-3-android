package com.dnd.safety.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Sample(
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: String,
) {
    val displayName: String
        get() = "[$id] $name"
}