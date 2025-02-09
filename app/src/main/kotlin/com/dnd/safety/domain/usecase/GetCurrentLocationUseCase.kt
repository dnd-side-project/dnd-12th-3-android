package com.dnd.safety.domain.usecase

import com.dnd.safety.data.model.Location
import com.dnd.safety.domain.repository.LocationRepository
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Result<Location> =
        locationRepository.getCurrentLocation()
}