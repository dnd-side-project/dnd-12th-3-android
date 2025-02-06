package com.dnd.safety.presentation.ui.home.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.IncidentTypeFilter
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.domain.model.SortFilter

@Stable
sealed interface IncidentsState {

    @Immutable
    data object Loading : IncidentsState

    @Immutable
    data class Success(
        val incidents: List<Incidents>
    ) : IncidentsState {

    }
}