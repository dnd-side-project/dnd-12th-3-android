package com.dnd.safety.domain.model

import java.time.LocalDate

data class MyReport(
    val id: Long,
    val title: String,
    val content: String,
    val imageUrl: String,
    val date: LocalDate
)