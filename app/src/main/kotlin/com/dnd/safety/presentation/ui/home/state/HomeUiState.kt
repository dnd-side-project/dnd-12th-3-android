package com.dnd.safety.presentation.ui.home.state

import androidx.compose.runtime.Immutable
import com.dnd.safety.domain.model.IncidentCategory
import com.dnd.safety.domain.model.IncidentTypeFilter

@Immutable
data class HomeUiState(
    val typeFilters: List<IncidentTypeFilter> = IncidentTypeFilter.entries,
) {
    val selectedType: IncidentCategory
        get() = typeFilters.firstOrNull { it.isSelected }?.incidentCategory ?: IncidentCategory.ALL
}

