package com.dnd.safety.data.repository

import com.dnd.safety.data.model.request.IncidentRequestDto
import com.dnd.safety.data.remote.api.IncidentService
import com.dnd.safety.domain.model.IncidentReport
import com.dnd.safety.domain.repository.IncidentRepository
import com.dnd.safety.utils.FileManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class IncidentRepositoryImpl @Inject constructor(
    private val incidentService: IncidentService,
    private val fileManager: FileManager,
    private val gson: Gson
) : IncidentRepository {

    override suspend fun createIncident(incidentReport: IncidentReport): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val incidentDto = IncidentRequestDto.from(incidentReport)

                val imageParts = incidentReport.images.map { uri ->
                    fileManager.getMultipartBody(uri)
                }

                val response = incidentService.createIncident(
                    incidentData = incidentDto,
                    files = imageParts
                )

                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to create incident: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}