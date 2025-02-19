package com.dnd.safety.domain.usecase

import com.dnd.safety.domain.model.IncidentReport
import com.dnd.safety.domain.repository.IncidentRepository
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class CreateIncidentReportUseCase @Inject constructor(
    private val incidentRepository: IncidentRepository
) {
    suspend operator fun invoke(incidentReport: IncidentReport): ApiResponse<Unit> {
        return incidentRepository.createIncident(incidentReport)
    }
}