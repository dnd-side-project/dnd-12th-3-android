package com.dnd.safety.domain.model

import com.google.android.gms.maps.model.LatLng

data class MyTown(
    val id: Long,
    val title: String,
    val address: String,
    val point: Point,
    val selected: Boolean
) {
    val latLng: LatLng
        get() = LatLng(point.y, point.x)
}

