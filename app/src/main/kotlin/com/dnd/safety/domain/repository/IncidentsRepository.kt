package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.Incidents
import com.google.android.gms.maps.model.LatLng
import com.skydoves.sandwich.ApiResponse

interface IncidentsRepository {

    suspend fun getIncidents(
        location: LatLng
    ): ApiResponse<List<Incidents>>
}