package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.IncidentReport
import com.skydoves.sandwich.ApiResponse

interface IncidentRepository {

    suspend fun createIncident(incidentReport: IncidentReport): ApiResponse<Unit>
}