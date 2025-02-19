package com.dnd.safety.data.repository

import com.dnd.safety.data.model.request.IncidentRequestDto
import com.dnd.safety.data.remote.api.IncidentService
import com.dnd.safety.domain.model.IncidentReport
import com.dnd.safety.domain.repository.IncidentRepository
import com.dnd.safety.utils.FileManager
import com.google.gson.Gson
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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
            ApiResponse.Failure.Error(e)
        }
    }
}