package com.dnd.safety.domain.model

import java.time.LocalDateTime

data class Comment(
    val id: Int,
    val writerId: Int,
    val writerName: String,
    val comment: String,
    val date: LocalDateTime,
    val isMyComment: Boolean = false
)

data class Comments(
    val comments: List<Comment>,
    val nextCursor: Long
)