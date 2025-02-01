package com.dnd.safety.data.repository

import com.dnd.safety.data.model.request.IncidentsRequest
import com.dnd.safety.data.remote.api.IncidentsApi
import com.dnd.safety.domain.mapper.toIncidentsList
import com.dnd.safety.domain.model.BoundingBox
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.domain.repository.IncidentsRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import javax.inject.Inject

class IncidentsRepositoryImpl @Inject constructor(
    private val incidentsApi: IncidentsApi
) : IncidentsRepository {

    override suspend fun getIncidents(
        boundingBox: BoundingBox
    ): ApiResponse<List<Incidents>> {
        return incidentsApi.getIncidents(
            IncidentsRequest(
                pointTopRightX = boundingBox.topRight.x,
                pointTopRightY = boundingBox.topRight.y,
                pointBottomLeftX = boundingBox.bottomLeft.x,
                pointBottomLeftY = boundingBox.bottomLeft.y
            )
        ).mapSuccess {
            toIncidentsList()
        }
    }
}