package com.dnd.safety.presentation.ui.home.effect

import com.dnd.safety.domain.model.Incident

sealed interface HomeUiEffect {
    data class ShowIncidentDetail(val incident: Incident) : HomeUiEffect
}
    