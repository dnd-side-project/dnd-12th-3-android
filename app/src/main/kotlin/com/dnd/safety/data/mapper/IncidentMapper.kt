package com.dnd.safety.data.mapper

import com.dnd.safety.data.model.response.IncidentResponse
import com.dnd.safety.data.model.response.MediaFileDto
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.model.IncidentCategory
import com.dnd.safety.utils.toLocalDateTime

fun IncidentResponse.Data.toIncident() = Incident(
    id = id,
    title = locationInfoName,
    description = description,
    longitude = longitude,
    latitude = latitude,
    incidentCategory = IncidentCategory.fromString(incidentCategory),
    userName = writerName,
    roadNameAddress = roadNameAddress,
    lotNumberAddress = lotNumberAddress,
    likeCount = likeCount,
    commentCount = commentCount,
    liked = liked,
    editable = editable,
    mediaFiles = mediaFiles.map(MediaFileDto::toMediaFile),
    createdDate = createdAt.toLocalDateTime(),
    updatedDate = updatedAt.toLocalDateTime(),
    distance = distance
)