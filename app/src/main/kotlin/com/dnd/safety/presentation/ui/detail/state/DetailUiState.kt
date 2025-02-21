package com.dnd.safety.presentation.ui.detail.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.Incident

@Stable
sealed interface DetailUiState {

    @Immutable
    data object Loading : DetailUiState

    @Immutable
    data class IncidentDetail(val incident: Incident) : DetailUiState
}