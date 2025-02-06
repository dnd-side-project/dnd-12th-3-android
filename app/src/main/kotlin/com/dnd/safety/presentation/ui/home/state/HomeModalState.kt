package com.dnd.safety.presentation.ui.home.state

import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.SortFilter

@Stable
sealed interface HomeModalState {

    data object Dismiss : HomeModalState

    data class ShowSortSheet(val sortFilters: List<SortFilter>) : HomeModalState
}