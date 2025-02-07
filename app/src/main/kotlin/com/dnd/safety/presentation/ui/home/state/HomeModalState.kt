package com.dnd.safety.presentation.ui.home.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.SortFilter

@Stable
sealed interface HomeModalState {

    @Immutable
    data object Dismiss : HomeModalState

    @Immutable
    data class ShowSortSheet(val sortFilters: List<SortFilter>) : HomeModalState

    @Immutable
    data object ShowSearchDialog : HomeModalState
}