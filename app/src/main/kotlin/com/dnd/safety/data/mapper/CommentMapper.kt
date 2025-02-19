package com.dnd.safety.data.mapper

import com.dnd.safety.data.model.response.CommentResponse
import com.dnd.safety.domain.model.Comment
import com.dnd.safety.domain.model.Comments
import com.dnd.safety.utils.toLocalDateTime
import java.time.LocalDateTime

fun CommentResponse.toComments() = Comments(
    comments = data.contents.map {
        Comment(
            commentId = it.commentId,
            comment = it.content,
            writerName = it.writerName,
            isMyComment = it.editable,
            date = it.createdAt.toLocalDateTime()
        )
    },
    nextCursor = data.nextCursorRequest.key
)