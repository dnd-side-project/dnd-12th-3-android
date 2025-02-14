package com.dnd.safety.presentation.ui.home.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface HomeModalState {

    @Immutable
    data object Dismiss : HomeModalState

    @Immutable
    data object ShowSearchDialog : HomeModalState
}