package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.IncidentReport

interface IncidentRepository {

    suspend fun createIncident(incidentReport: IncidentReport): Result<Unit>
}