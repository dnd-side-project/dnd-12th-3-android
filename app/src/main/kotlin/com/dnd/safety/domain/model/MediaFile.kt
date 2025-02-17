package com.dnd.safety.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MediaFile(
    val incidentId: Int,
    val mediaType: String,
    val fileUrl: String
)
