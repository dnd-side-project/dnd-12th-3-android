package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.Incidents
import com.skydoves.sandwich.ApiResponse

interface IncidentsRepository {

    suspend fun getIncidents(): ApiResponse<List<Incidents>>
}