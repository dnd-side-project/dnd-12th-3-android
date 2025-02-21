package com.dnd.safety.domain.model

import java.time.LocalDateTime

data class FcmMessage(
    val id: Long,
    val title: String,
    val content: String,
    val image: String,
    val createdAt: LocalDateTime
)
