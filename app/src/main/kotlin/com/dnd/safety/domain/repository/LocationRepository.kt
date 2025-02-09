package com.dnd.safety.domain.repository

import com.dnd.safety.data.model.Location

interface LocationRepository {
    suspend fun fetchLocationData(query: String): List<Location>
    suspend fun getCurrentLocation(): Result<Location>
}