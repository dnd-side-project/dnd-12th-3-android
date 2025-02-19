package com.dnd.safety.data.mapper

import com.dnd.safety.data.model.response.IncidentData
import com.dnd.safety.data.model.response.IncidentsResponse
import com.dnd.safety.data.model.response.MediaFileDto
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.model.IncidentCategory
import com.dnd.safety.domain.model.MediaFile
import com.dnd.safety.domain.model.MyReports
import com.dnd.safety.utils.toLocalDateTime

fun IncidentsResponse.toIncidentsList() = this.data.map { it.toIncidents() }

fun IncidentData.toIncidents(): Incident = Incident(
    id = incident.id,
    title = incident.locationInfoName,
    description = incident.description,
    longitude = incident.longitude,
    latitude = incident.latitude,
    incidentCategory = IncidentCategory.fromString(incident.incidentCategory),
    userName = writer.nickname,
    distance = distance,
    roadNameAddress = incident.roadNameAddress,
    lotNumberAddress = incident.lotNumberAddress,
    likeCount = incident.likeCount,
    commentCount = incident.commentCount,
    createdDate = incident.createdAt.toLocalDateTime(),
    updatedDate = incident.updatedAt.toLocalDateTime(),
    mediaFiles = mediaFiles.map(MediaFileDto::toMediaFile)
)

fun MediaFileDto.toMediaFile() = MediaFile(
    incidentId = incidentId,
    mediaType = mediaType,
    fileUrl = fileUrl
)

fun extractDong(address: String): String? {
    val regex = """(\S+동)""".toRegex()
    return regex.find(address)?.value
}