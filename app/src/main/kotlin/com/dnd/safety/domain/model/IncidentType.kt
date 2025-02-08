package com.dnd.safety.domain.model

data class IncidentTypeFilter(
    val incidentCategory: IncidentCategory,
    val isSelected: Boolean
) {

    companion object {
        val entries = IncidentCategory.entries.map {
            IncidentTypeFilter(
                incidentCategory = it,
                isSelected = it == IncidentCategory.ALL
            )
        }
    }
}