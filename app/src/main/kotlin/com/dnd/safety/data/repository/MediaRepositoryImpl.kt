package com.dnd.safety.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dnd.safety.data.datasorce.MediaPagingSource
import com.dnd.safety.domain.model.Media
import com.dnd.safety.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val mediaPagingSource: MediaPagingSource
) : MediaRepository {
    override fun getMediaFromGallery(): Flow<PagingData<Media>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false,
                initialLoadSize = 30,
                maxSize = 90
            ),
            pagingSourceFactory = { mediaPagingSource }
        ).flow
    }
}