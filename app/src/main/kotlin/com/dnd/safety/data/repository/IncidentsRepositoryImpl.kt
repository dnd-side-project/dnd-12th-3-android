package com.dnd.safety.data.repository

import com.dnd.safety.data.remote.api.IncidentsApi
import com.dnd.safety.domain.mapper.toIncidentsList
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.domain.repository.IncidentsRepository
import com.google.android.gms.maps.model.LatLng
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import javax.inject.Inject

class IncidentsRepositoryImpl @Inject constructor(
    private val incidentsApi: IncidentsApi
) : IncidentsRepository {

    override suspend fun getIncidents(
        location: LatLng
    ): ApiResponse<List<Incidents>> {
        return incidentsApi.getIncidents(
            location.longitude,
            location.latitude,
        ).mapSuccess {
            toIncidentsList()
        }
    }
}