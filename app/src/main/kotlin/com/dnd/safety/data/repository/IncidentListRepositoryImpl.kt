package com.dnd.safety.data.repository

import com.dnd.safety.data.remote.api.IncidentsApi
import com.dnd.safety.domain.mapper.toIncidentsList
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.domain.repository.IncidentListRepository
import com.dnd.safety.utils.Logger
import com.google.android.gms.maps.model.LatLng
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import javax.inject.Inject

class IncidentListRepositoryImpl @Inject constructor(
    private val incidentsApi: IncidentsApi
) : IncidentListRepository {

    override suspend fun getIncidents(
        location: LatLng
    ): ApiResponse<List<Incidents>> {
        return incidentsApi.getIncidents(
            location.longitude,
            location.latitude,
        ).mapSuccess {
            toIncidentsList()
        }.onFailure {
            Logger.e(message())
        }
    }

    override suspend fun getMyIncidents(): ApiResponse<List<Incidents>> {
        TODO("Not yet implemented")
    }
}