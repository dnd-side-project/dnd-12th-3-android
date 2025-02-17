package com.dnd.safety.domain.model

import com.google.android.gms.maps.model.LatLng

data class Point(
    val x: Double,
    val y: Double
) {
    constructor(latLng: LatLng) : this(latLng.longitude, latLng.latitude)
}