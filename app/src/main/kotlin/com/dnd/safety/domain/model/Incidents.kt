package com.dnd.safety.domain.model

import com.dnd.safety.utils.daysAgo
import java.time.LocalDateTime

data class Incidents(
    val id: Long,
    val title: String,
    val userName: String,
    val distance: String,
    val description: String,
    val address: String,
    val pointX: Double,
    val pointY: Double,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime,
    val incidentCategory: IncidentCategory,
    val mediaFiles: List<MediaFile>,
) {

    val daysAgo get() = "${createdDate.daysAgo()}일 전"

    companion object {
        val sampleIncidents = listOf(
            Incidents(
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
