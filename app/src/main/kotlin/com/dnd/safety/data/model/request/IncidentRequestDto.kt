package com.dnd.safety.data.model.request

import com.dnd.safety.domain.model.IncidentReport
import kotlinx.serialization.Serializable

@Serializable
data class IncidentRequestDto(
    val writerId: Long,
    val description: String,
    val disasterGroup: String,
    val pointX: Double,
    val pointY: Double
) {
    companion object {
        fun from(domain: IncidentReport) = IncidentRequestDto(
            writerId = domain.writerId,
            description = domain.description,
            disasterGroup = mapCategoryToDisasterGroup(domain.disasterGroup),
            pointX = domain.location.longitude,
            pointY = domain.location.latitude
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