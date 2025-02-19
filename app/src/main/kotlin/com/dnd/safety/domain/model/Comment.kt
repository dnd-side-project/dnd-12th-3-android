package com.dnd.safety.domain.model

import java.time.LocalDateTime

data class Comment(
    val commentId: Long,
    val comment: String,
    val writerName: String,
    val date: LocalDateTime,
    val isMyComment: Boolean = false
)

data class Comments(
    val comments: List<Comment>,
    val nextCursor: Long
)