package com.dnd.safety.presentation.ui.home.state

import androidx.compose.runtime.Immutable
import com.dnd.safety.domain.model.IncidentTypeFilter
import com.dnd.safety.domain.model.SortFilter

@Immutable
data class HomeUiState(
    val isCurrentLocation: Boolean = true,
    val keyword: String = "",
    val typeFilters: List<IncidentTypeFilter> = IncidentTypeFilter.entries,
    val sortFilters: List<SortFilter> = SortFilter.entries,
) {

    val selectedSort: SortFilter
        get() = sortFilters.first { it.isSelected }
}

