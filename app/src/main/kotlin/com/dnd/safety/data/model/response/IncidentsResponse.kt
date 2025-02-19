package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class IncidentsResponse(
    val code: String = "",
    val data: List<IncidentData>
)

@Serializable
data class IncidentData(
    val writer: WriterDto,
    val incident: IncidentDto,
    val distance: String = "",
    val mediaFiles: List<MediaFileDto>
)

@Serializable
data class IncidentDto(
    val id: Long,
    val writerId: Long,
    val description: String,
    val locationInfoName: String,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val incidentCategory: String,
    val latitude: Double,
    val longitude: Double,
    val commentCount: Int,
    val likeCount: Int,
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