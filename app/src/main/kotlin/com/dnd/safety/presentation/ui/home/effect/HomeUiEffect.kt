package com.dnd.safety.presentation.ui.home.effect

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.Incident
import com.google.android.gms.maps.model.LatLng

@Stable
sealed interface HomeUiEffect {

    @Immutable
    data class ShowIncidentDetail(val incident: Incident) : HomeUiEffect

    @Immutable
    data class MoveCameraToLocation(val latLng: LatLng) : HomeUiEffect
}
    