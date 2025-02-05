package com.dnd.safety.presentation.ui.locationsearch

import com.dnd.safety.data.model.Location

data class LocationSearchState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val locations: List<Location> = emptyList(),
    val error: String? = null,
    val hasSearched: Boolean = false,
)