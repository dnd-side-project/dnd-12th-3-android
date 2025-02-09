package com.dnd.safety.domain.model

import android.net.Uri

data class Media(
    val uri: Uri,
    val type: MediaType,
    val thumbnailUri: Uri? = null
)

enum class MediaType {
    IMAGE, VIDEO
}