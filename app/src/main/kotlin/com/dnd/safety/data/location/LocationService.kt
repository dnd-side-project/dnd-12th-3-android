package com.dnd.safety.data.location

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationService {
    fun requestLocationUpdates(
        intervalMillis: Long = 100000L
    ): Flow<LatLng?>

    suspend fun getCurrentLocation(): LatLng?
}