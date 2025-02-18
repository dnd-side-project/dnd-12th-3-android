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
        val incidentId: Int,
        val id: Int,
        val writerId: Int,
        val writerName: String = "",
        val content: String,
        val parentId: Long? = null,
        val children: Long? = null,
    )
}