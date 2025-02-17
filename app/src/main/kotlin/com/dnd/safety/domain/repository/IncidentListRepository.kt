package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.Incident
import com.google.android.gms.maps.model.LatLng
import com.skydoves.sandwich.ApiResponse

interface IncidentListRepository {

    suspend fun getIncidents(
        location: LatLng
    ): ApiResponse<List<Incident>>

    suspend fun getMyIncidents(): ApiResponse<List<Incident>>
}