package com.dnd.safety.presentation.ui.home.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.Incident

@Stable
sealed interface IncidentsState {

    @Immutable
    data object Loading : IncidentsState

    @Immutable
    data class Success(
        val incidents: List<Incident>
    ) : IncidentsState {

    }
}