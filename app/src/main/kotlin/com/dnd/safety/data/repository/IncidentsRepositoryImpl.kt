package com.dnd.safety.data.repository

import com.dnd.safety.data.model.request.IncidentsRequest
import com.dnd.safety.data.remote.api.IncidentsApi
import com.dnd.safety.domain.mapper.toIncidentsList
import com.dnd.safety.domain.model.BoundingBox
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.domain.repository.IncidentsRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import retrofit2.Response
import javax.inject.Inject

class IncidentsRepositoryImpl @Inject constructor(
    private val incidentsApi: IncidentsApi
) : IncidentsRepository {

    override suspend fun getIncidents(
        boundingBox: BoundingBox
    ): ApiResponse<List<Incidents>> {
        return ApiResponse.Success(Response.success(emptyList()))
    }
}