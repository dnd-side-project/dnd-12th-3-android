package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val code: String,
    val `data`: Data,
    val timestamp: String
) {

    @Serializable
    data class Data(
        val contents: List<Content>,
        val nextCursorRequest: NextCursorRequest
    )

    @Serializable
    data class Content(
        val commentId: Long,
        val content: String,
        val writerName: String = "",
        val parentId: Long? = null,
        val editable: Boolean = false,
        val createdAt: String = "",
    )
}