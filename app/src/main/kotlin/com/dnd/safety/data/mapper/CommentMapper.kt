package com.dnd.safety.data.mapper

import com.dnd.safety.data.model.response.CommentResponse
import com.dnd.safety.domain.model.Comment
import com.dnd.safety.domain.model.Comments
import java.time.LocalDateTime

fun CommentResponse.toComments() = Comments(
    comments = data.contents.map {
        Comment(
            id = it.id,
            writerId = it.writerId,
            writerName = it.writerName,
            comment = it.content,
            date = LocalDateTime.now()
        )
    },
    nextCursor = data.nextCursorRequest.key
)