package com.dnd.safety.data.model.request

import com.dnd.safety.domain.model.IncidentReport
import kotlinx.serialization.Serializable

@Serializable
data class IncidentRequestDto(
    val locationInfoName: String,
    val lotNumberAddress: String,
    val roadNameAddress: String,
    val description: String,
    val incidentCategory: String,
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        fun from(domain: IncidentReport) = IncidentRequestDto(
            locationInfoName = domain.location.placeName,
            lotNumberAddress = domain.location.lotNumberAddress,
            roadNameAddress = domain.location.roadNameAddress,
            description = domain.description,
            incidentCategory = mapCategoryToDisasterGroup(domain.disasterGroup),
            longitude = domain.location.longitude,
            latitude = domain.location.latitude
        )

        private fun mapCategoryToDisasterGroup(category: String): String {
            return when (category) {
                "TRAFFIC" -> "교통"
                "FIRE" -> "화재"
                "COLLAPSE" -> "붕괴"
                "EXPLOSION" -> "폭발"
                "NATURAL" -> "자연재난"
                "DUST" -> "미세먼지"
                "TERROR" -> "테러"
                else -> throw IllegalArgumentException("Invalid category: $category")
            }
        }
    }
}