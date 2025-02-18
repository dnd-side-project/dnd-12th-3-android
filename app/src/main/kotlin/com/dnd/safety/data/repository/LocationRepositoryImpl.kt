package com.dnd.safety.data.repository

import android.location.LocationManager
import android.util.Log
import com.dnd.safety.data.datasorce.AddressNotFoundException
import com.dnd.safety.data.datasorce.LocationNotFoundException
import com.dnd.safety.data.model.Location
import com.dnd.safety.data.model.response.KakaoLocationResponse
import com.dnd.safety.data.remote.api.LocationService
import com.dnd.safety.domain.datasource.LocationDataSource
import com.dnd.safety.data.mapper.LocationMapper.toLocation
import com.dnd.safety.domain.repository.LocationRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationService: LocationService,
    private val locationDataSource: LocationDataSource,
): LocationRepository {

    override suspend fun fetchLocationData(query: String): List<Location> {
        return locationService.searchToQueryLocation(query = query)
            .documents
            .map { it.toLocation() }
    }

    override suspend fun getCurrentLocation(): Result<Location> {
        return when (val response = locationDataSource.getCurrentLocation()) {
            is ApiResponse.Success -> {
                try {
                    val data = response.data
                    val document = data.addressResponse.documents.firstOrNull()
                        ?: return Result.failure(AddressNotFoundException("주소를 찾을 수 없습니다"))

                    Result.success(Location(
                        latitude = data.latitude,
                        longitude = data.longitude,
                        placeName = document.road_address?.building_name.orEmpty(),
                        address = document.road_address?.address_name
                            ?: document.address?.address_name.orEmpty()
                    ))
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            is ApiResponse.Failure.Error -> {
                Result.failure(Exception(response.message()))
            }
            is ApiResponse.Failure.Exception -> {
                Result.failure(response.throwable)
            }
        }
    }
}
