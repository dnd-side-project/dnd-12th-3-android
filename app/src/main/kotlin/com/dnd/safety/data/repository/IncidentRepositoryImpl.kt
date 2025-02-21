package com.dnd.safety.data.repository

import com.dnd.safety.data.mapper.toIncident
import com.dnd.safety.data.mapper.toIncidents
import com.dnd.safety.data.model.request.IncidentRequestDto
import com.dnd.safety.data.remote.api.IncidentService
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.model.IncidentReport
import com.dnd.safety.domain.repository.IncidentRepository
import com.dnd.safety.utils.FileManager
import com.dnd.safety.utils.Logger
import com.google.android.gms.maps.model.LatLng
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import javax.inject.Inject

class IncidentRepositoryImpl @Inject constructor(
    private val incidentService: IncidentService,
    private val fileManager: FileManager
) : IncidentRepository {

    override suspend fun createIncident(incidentReport: IncidentReport): ApiResponse<Unit> {
        return try {
            val incidentDto = IncidentRequestDto.from(incidentReport)

            val imageParts = incidentReport.images.map { uri ->
                fileManager.getMultipartBody(uri)
            }

            incidentService.createIncident(
                incidentData = incidentDto,
                files = imageParts
            )
        } catch (e: Exception) {
            Logger.e("createIncident: ${e.message}")
            ApiResponse.Failure.Error(e)
        }
    }

    override suspend fun updateIncident(incidentReport: IncidentReport): ApiResponse<Unit> {
        return try {
            val incidentDto = IncidentRequestDto.from(incidentReport)

            val imageParts = incidentReport.images.map { uri ->
                fileManager.getMultipartBody(uri)
            }

            incidentService.updateIncident(
                incidentId = incidentReport.id!!,
                incidentData = incidentDto,
                files = imageParts
            ).mapSuccess { }
        } catch (e: Exception) {
            Logger.e("updateIncident: ${e.message}")
            ApiResponse.Failure.Error(e)
        }

    }

    override suspend fun deleteIncident(incidentId: Long): ApiResponse<Unit> {
        return incidentService.deleteIncident(incidentId)
            .mapSuccess { }
            .onFailure {
                Logger.e("deleteIncident: ${message()}")
            }
    }

    override suspend fun getIncidentData(
        incidentId: Long,
        myLocation: LatLng
    ): ApiResponse<Incident> {
        return incidentService
            .getIncidentData(
                incidentId,
                myLocation.longitude,
                myLocation.latitude
            )
            .mapSuccess {
                data.toIncident()
            }
            .onFailure {
                Logger.e("getIncidentData: ${message()}")
            }
    }
}