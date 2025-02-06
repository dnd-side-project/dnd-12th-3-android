package com.dnd.safety.domain.model

import com.dnd.safety.utils.daysAgo
import java.time.LocalDateTime

@Suppress("서버와 협의하여 수정되어야 함")
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
    val incidentType: IncidentType,
    val imageUrls: List<String>,
) {

    val daysAgo get() = "${createdDate.daysAgo()}일 전"

    companion object {
        val sampleIncidents = listOf(
            Incidents(
                id = 1,
                title = "title",
                description = "description",
                pointX = 0.0,
                pointY = 0.0,
                imageUrls = listOf("https://www.example.com"),
                distance = "distance",
                userName = "userName",
                address = "address",
                incidentType = IncidentType.교통,
                createdDate = LocalDateTime.now(),
                updatedDate = LocalDateTime.now()
            )
        )
    }
}
