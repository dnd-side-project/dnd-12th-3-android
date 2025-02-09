package com.dnd.safety.presentation.ui.home.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.BoundingBox

@Stable
sealed interface BoundingBoxState {

    @Immutable
    data object NotInitialized : BoundingBoxState

    @Immutable
    data class Success(val boundingBox: BoundingBox) : BoundingBoxState
}