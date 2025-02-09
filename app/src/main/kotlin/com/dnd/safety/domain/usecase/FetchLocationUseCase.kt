package com.dnd.safety.domain.usecase

import com.dnd.safety.data.model.Location
import com.dnd.safety.domain.repository.LocationRepository
import javax.inject.Inject

class FetchLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
) {
    suspend operator fun invoke(query: String): Result<List<Location>> {
        return try {
            val locations = locationRepository.fetchLocationData(query)
            Result.success(locations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}