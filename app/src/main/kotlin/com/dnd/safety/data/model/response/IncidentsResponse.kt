package com.dnd.safety.data.model.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class IncidentsResponse(
    val code: String = "",
    val data: List<Data>
) {
    @Serializable
    data class Data(
        val writer: WriterDto,
        val incident: IncidentDto,
        val distance: String,
        val mediaFiles: List<MediaFileDto>
    )

    @Serializable
    data class IncidentDto(
        val id: Long,
        val writerId: Long,
        val description: String,
        val roadNameAddress: String,
        val disasterGroup: String,
        val pointX: Double,
        val pointY: Double,
        @SerializedName("createdAt") val createdDate: String,
        @SerializedName("updatedAt") val updatedDate: String,
    )

    @Serializable
    data class MediaFileDto(
        val incidentId: Int,
        val mediaType: String,
        val fileUrl: String,
    )

    @Serializable
    data class WriterDto(
        val nickname: String,
    )
}
