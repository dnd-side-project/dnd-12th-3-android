package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.model.IncidentReport
import com.skydoves.sandwich.ApiResponse

interface IncidentRepository {

    suspend fun createIncident(incidentReport: IncidentReport): ApiResponse<Unit>

    suspend fun updateIncident(incidentReport: IncidentReport): ApiResponse<Unit>

    suspend fun deleteIncident(incidentId: Long): ApiResponse<Unit>

    suspend fun getIncidentData(incidentId: Long): ApiResponse<Incident>
}