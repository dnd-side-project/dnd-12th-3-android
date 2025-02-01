package com.dnd.safety.domain.model

import java.time.LocalDate

@Suppress("서버와 협의하여 수정되어야 함")
data class Incidents(
    val id: Int,
    val title: String,
    val description: String,
    val pointX: Double,
    val pointY: Double,
    val createdDate: LocalDate,
    val updatedDate: LocalDate,

    val imageUrl: String,
)
