package com.dnd.safety.domain.model

import java.time.LocalDateTime

data class Comment(
    val name: String,
    val comment: String,
    val date: LocalDateTime
)
