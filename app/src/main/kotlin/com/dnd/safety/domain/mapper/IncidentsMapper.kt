package com.dnd.safety.domain.mapper

import com.dnd.safety.data.model.response.IncidentsResponse
import com.dnd.safety.domain.model.IncidentCategory
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.domain.model.MediaFile
import com.dnd.safety.utils.toLocalDateTime

fun IncidentsResponse.toIncidentsList() = this.data.map { it.toIncidents() }

fun IncidentsResponse.IncidentDto.toIncidents(): Incidents = Incidents(
    id = id,
    title = title,
    description = description,
    pointX = pointX,
    pointY = pointY,
    incidentCategory = IncidentCategory.fromString(disasterGroup),
    userName = "userName",
    distance = "distance",
    address = "address",
    createdDate = createdDate.toLocalDateTime(),
    updatedDate = updatedDate.toLocalDateTime(),
    mediaFiles = mediaFiles.map(IncidentsResponse.MediaFileDto::toMediaFile)
)

fun IncidentsResponse.MediaFileDto.toMediaFile() = MediaFile(
    incidentId = incidentId,
    mediaType = mediaType,
    fileUrl = fileUrl
)