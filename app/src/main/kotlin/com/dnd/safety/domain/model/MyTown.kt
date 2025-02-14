package com.dnd.safety.domain.model

import com.google.android.gms.maps.model.LatLng

data class MyTown(
    val id: Long,
    val title: String,
    val latLng: LatLng,
    val selected: Boolean
)
