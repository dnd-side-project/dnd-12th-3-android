package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class IncidentResponse(
    val code: String = "",
    val timestamp: String = "",
    val `data`: Data,
) {

    @Serializable
    data class Data(
        val commentCount: Int,
        val description: String,
        val editable: Boolean,
        val id: Long,
        val incidentCategory: String,
        val latitude: Double,
        val likeCount: Int,
        val liked: Boolean,
        val locationInfoName: String,
        val longitude: Double,
        val lotNumberAddress: String,
        val mediaFiles: List<MediaFileDto>,
        val roadNameAddress: String,
        val writerId: Int,
        val writerName: String
    )
}