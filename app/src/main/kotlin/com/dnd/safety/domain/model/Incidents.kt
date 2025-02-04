package com.dnd.safety.domain.model

import java.time.LocalDateTime

@Suppress("서버와 협의하여 수정되어야 함")
data class Incidents(
    val id: Long,
    val title: String,
    val description: String,
    val pointX: Double,
    val pointY: Double,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime,

    val imageUrl: String,
) {

    companion object {
        val sampleIncidents = listOf(
            Incidents(
                id = 1,
                title = "title",
                description = "description",
                pointX = 0.0,
                pointY = 0.0,
                imageUrl = "imageUrl",
                createdDate = LocalDateTime.now(),
                updatedDate = LocalDateTime.now()
            )
        )
    }
}
