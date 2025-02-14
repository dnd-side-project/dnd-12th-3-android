package com.dnd.safety.presentation.ui.postreport.photoSelection

import androidx.paging.PagingData
import com.dnd.safety.domain.model.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PhotoSelectionState(
    val mediaFlow: Flow<PagingData<Media>> = emptyFlow(),
    val selectedMedia: List<Media> = emptyList(),
    val isLoading: Boolean = true,
    val isPermissionGranted: Boolean? = null,
)