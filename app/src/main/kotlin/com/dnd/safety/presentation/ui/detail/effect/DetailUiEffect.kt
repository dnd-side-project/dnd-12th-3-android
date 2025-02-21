package com.dnd.safety.presentation.ui.detail.effect

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.Incident

@Stable
interface DetailUiEffect {

    @Immutable
    data class ShowSnackBar(val message: String) : DetailUiEffect

    @Immutable
    data object HideKeyboard : DetailUiEffect

    @Immutable
    data class NavigateToIncidentEdit(val incident: Incident) : DetailUiEffect

    @Immutable
    data object NavigateToBack : DetailUiEffect
}