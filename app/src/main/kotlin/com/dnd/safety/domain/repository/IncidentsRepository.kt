package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.BoundingBox
import com.dnd.safety.domain.model.Incidents
import com.skydoves.sandwich.ApiResponse

interface IncidentsRepository {

    suspend fun getIncidents(
        boundingBox: BoundingBox
    ): ApiResponse<List<Incidents>>
}