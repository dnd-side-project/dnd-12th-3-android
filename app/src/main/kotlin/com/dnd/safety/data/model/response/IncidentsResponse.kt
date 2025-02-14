package com.dnd.safety.data.model.response

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
        val createdAt: String,
        val updatedAt: String,
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
