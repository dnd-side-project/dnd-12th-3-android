package com.dnd.safety.presentation.ui.myPage.effect

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.Incident

@Stable
interface MyReportUiEffect {

    @Immutable
    data object NavigateBack : MyReportUiEffect

    @Immutable
    data class ShowSnackBar(val message: String) : MyReportUiEffect

    @Immutable
    data class NavigateToIncidentEdit(val incident: Incident) : MyReportUiEffect
}