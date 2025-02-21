package com.dnd.safety.data.mapper

import com.dnd.safety.data.local.entity.FcmEntity
import com.dnd.safety.domain.model.FcmMessage

fun FcmEntity.toFcmMessage(): FcmMessage {
    return FcmMessage(
        id = id,
        title = title,
        content = content,
        image = imageUrl,
        createdAt = createAt
    )
}