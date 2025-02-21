package com.dnd.safety.data.model

import com.dnd.safety.domain.model.Incident
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val placeName: String,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val latitude: Double,
    val longitude: Double,
) {

    constructor(incident: Incident) : this(
        placeName = incident.title,
        roadNameAddress = incident.roadNameAddress,
        lotNumberAddress = incident.lotNumberAddress,
        latitude = incident.latitude,
        longitude = incident.longitude,
    )
}