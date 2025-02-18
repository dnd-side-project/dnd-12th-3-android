package com.dnd.safety.domain.model

import com.dnd.safety.presentation.navigation.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.LocalDateTime

@Serializable
data class Incident(
    val id: Long,
    val title: String,
    val userName: String,
    val distance: String,
    val description: String,
    val address: String,
    val pointX: Double,
    val pointY: Double,
    @Serializable(with = LocalDateTimeSerializer::class) val createdDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class) val updatedDate: LocalDateTime,
    val incidentCategory: IncidentCategory,
    val mediaFiles: List<MediaFile>,
) {

    val firstImage get() = mediaFiles.firstOrNull { it.mediaType == "image" }?.fileUrl

    companion object {
        val sampleIncidents = listOf(
            Incident(
                id = 1,
                title = "title",
                description = "description",
                pointX = 37.0,
                pointY = 127.0,
                mediaFiles = listOf(MediaFile(1, "mediaType", "fileUrl")),
                distance = "distance",
                userName = "userName",
                address = "address",
                incidentCategory = IncidentCategory.FIRE,
                createdDate = LocalDateTime.now(),
                updatedDate = LocalDateTime.now()
            )
        )
    }
}
