package com.dnd.safety.domain.usecase

import androidx.paging.PagingData
import com.dnd.safety.domain.model.Media
import com.dnd.safety.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaFromGalleryUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(): Flow<PagingData<Media>> {
        return mediaRepository.getMediaFromGallery()
    }
}