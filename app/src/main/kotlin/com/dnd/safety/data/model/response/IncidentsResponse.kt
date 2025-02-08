package com.dnd.safety.data.model.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class IncidentsResponse(
    val code: String = "",
    val timestamp: String = "",
    val data: List<IncidentDto>
) {

    @Serializable
    data class IncidentDto(
        val id: Long,
        val writerId: Long,
        val title: String,
        val description: String,
        val disasterGroup: String,
        val pointX: Double,
        val pointY: Double,
        @SerializedName("createdAt") val createdDate: String,
        @SerializedName("updatedAt") val updatedDate: String,
        val mediaFiles: List<MediaFileDto>
    )

    @Serializable
    data class MediaFileDto(
        val incidentId: Int,
        val mediaType: String,
        val fileUrl: String,
    )
}
