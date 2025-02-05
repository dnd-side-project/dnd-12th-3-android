package com.dnd.safety.data.repository

import com.dnd.safety.data.model.Location
import com.dnd.safety.data.remote.api.LocationService
import com.dnd.safety.domain.mapper.LocationMapper.toLocation
import com.dnd.safety.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationService: LocationService,
): LocationRepository {
    override suspend fun fetchLocationData(query: String): List<Location> {
        return locationService.searchToQueryLocation(query = query)
            .documents
            .map { it.toLocation() }
    }
}
