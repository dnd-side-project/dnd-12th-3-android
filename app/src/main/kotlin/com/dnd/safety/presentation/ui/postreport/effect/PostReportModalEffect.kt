package com.dnd.safety.presentation.ui.postreport.effect

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface PostReportModalEffect {

    @Immutable
    data object Dismiss : PostReportModalEffect

    @Immutable
    data object ShowPhotoPickerDialog : PostReportModalEffect
}