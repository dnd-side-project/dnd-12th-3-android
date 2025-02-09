package com.dnd.safety.domain.repository

import androidx.paging.PagingData
import com.dnd.safety.domain.model.Media
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getMediaFromGallery(): Flow<PagingData<Media>>
}