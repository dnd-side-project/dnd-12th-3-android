package com.dnd.safety.domain.model

enum class IncidentType {
    전체,
    교통,
    화재,
    붕괴,
    폭발,
    자연재난,
    미세먼지,
    테러,
}

data class IncidentTypeFilter(
    val incidentType: IncidentType,
    val isSelected: Boolean
) {

    companion object {
        val entries = IncidentType.entries.map {
            IncidentTypeFilter(
                incidentType = it,
                isSelected = it == IncidentType.전체
            )
        }
    }
}