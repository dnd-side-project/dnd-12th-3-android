package com.dnd.safety.domain.mapper

import com.dnd.safety.data.model.response.IncidentsResponse
import com.dnd.safety.domain.model.IncidentType
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.utils.toLocalDateTime

fun IncidentsResponse.toIncidentsList() = this.incidents.map { it.toIncidents() }

fun IncidentsResponse.IncidentDto.toIncidents(): Incidents = Incidents(
    id = id,
    title = title,
    description = description,
    pointX = pointX,
    pointY = pointY,
    incidentType = IncidentType.붕괴,
    userName = "userName",
    distance = "distance",
    address = "address",
    createdDate = createdDate.toLocalDateTime(),
    updatedDate = updatedDate.toLocalDateTime(),
    imageUrls = emptyList()
)