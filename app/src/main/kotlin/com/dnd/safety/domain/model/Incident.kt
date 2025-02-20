package com.dnd.safety.domain.model

import com.dnd.safety.presentation.navigation.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Incident(
    val id: Long,
    val title: String,
    val userName: String,
    val distance: String,
    val description: String,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val latitude: Double,
    val longitude: Double,
    val likeCount: Int,
    val commentCount: Int,
    @Serializable(with = LocalDateTimeSerializer::class) val createdDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class) val updatedDate: LocalDateTime,
    val incidentCategory: IncidentCategory,
    val mediaFiles: List<MediaFile>,
    val liked: Boolean = false,
    val editable: Boolean = false,
) {

    val firstImage get() = mediaFiles.firstOrNull { it.mediaType == "image" }?.fileUrl

    companion object {
        val sampleIncidents = listOf(
            Incident(
                id = 1,
                title = "title",
                roadNameAddress = "address",
                lotNumberAddress = "address",
                description = "description",
                longitude = 37.0,
                latitude = 127.0,
                mediaFiles = listOf(MediaFile(1, "mediaType", "fileUrl")),
                distance = "distance",
                userName = "userName",
                incidentCategory = IncidentCategory.FIRE,
                likeCount = 1,
                commentCount = 1,
                createdDate = LocalDateTime.now(),
                updatedDate = LocalDateTime.now()
            )
        )

        fun List<Incident>.incidentFilter(incidentCategory: IncidentCategory) = this.filter {
            if (incidentCategory == IncidentCategory.ALL) true
            else it.incidentCategory == incidentCategory
        }
    }
}

data class IncidentList(
    val incidents: List<Incident>,
    val nextCursor: Long
)