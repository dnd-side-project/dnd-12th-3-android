package com.dnd.safety.domain.model

import com.google.android.gms.maps.model.LatLng

data class SearchResult(
    val title: String,
    val latLng: LatLng
)