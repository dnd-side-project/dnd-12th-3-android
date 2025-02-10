package com.dnd.safety.domain.mapper

import com.dnd.safety.data.model.response.IncidentsResponse
import com.dnd.safety.domain.model.IncidentCategory
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.domain.model.MediaFile
import com.dnd.safety.utils.toLocalDateTime

fun IncidentsResponse.toIncidentsList() = this.data.map { it.toIncidents() }

fun IncidentsResponse.Data.toIncidents(): Incidents = Incidents(
    id = incident.id,
    title = extractDong(incident.roadNameAddress) ?: "제목",
    description = incident.description,
    pointX = incident.pointX,
    pointY = incident.pointY,
    incidentCategory = IncidentCategory.fromString(incident.disasterGroup),
    userName = writer.nickname,
    distance = distance,
    address = incident.roadNameAddress,
    createdDate = incident.createdDate.toLocalDateTime(),
    updatedDate = incident.updatedDate.toLocalDateTime(),
    mediaFiles = mediaFiles.map(IncidentsResponse.MediaFileDto::toMediaFile)
)

fun IncidentsResponse.MediaFileDto.toMediaFile() = MediaFile(
    incidentId = incidentId,
    mediaType = mediaType,
    fileUrl = fileUrl
)

fun extractDong(address: String): String? {
    val regex = """(\S+동)""".toRegex()
    return regex.find(address)?.value
}