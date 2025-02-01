package com.dnd.safety.presentation.ui.home.component

import com.google.android.gms.maps.model.LatLng

sealed interface LocationSource {
    data object CurrentLocation : LocationSource
    data class Search(val location: LatLng) : LocationSource
}
