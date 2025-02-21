package com.dnd.safety.domain.model

import com.google.android.gms.maps.model.LatLng

data class MyTown(
    val id: Long = 0L,
    val title: String,
    val address: String,
    val point: Point,
    val sido: String = "",
    val sgg: String = "",
    val emd: String = "",

    val selected: Boolean = false
) {
    val latLng: LatLng
        get() = LatLng(point.y, point.x)
}

