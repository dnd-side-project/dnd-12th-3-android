package com.dnd.safety.data.datasorce

import android.annotation.SuppressLint
import android.location.LocationManager
import com.dnd.safety.data.model.KakaoAddressInfo
import com.dnd.safety.data.remote.api.LocationService
import com.dnd.safety.domain.datasource.LocationDataSource
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class LocationDataSourceImpl @Inject constructor(
    private val locationManager: LocationManager,
    private val kakaoLocationApi: LocationService,
) : LocationDataSource {


    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): ApiResponse<KakaoAddressInfo> {
        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            ?: throw LocationNotFoundException("현재 위치를 찾을 수 없습니다")

        when (val response = kakaoLocationApi.getLocationByCoord(
            longitude = lastLocation.longitude.toString(),
            latitude = lastLocation.latitude.toString()
        )) {
            is ApiResponse.Success -> {
                return ApiResponse.Success(
                    KakaoAddressInfo(
                        latitude = lastLocation.latitude,
                        longitude = lastLocation.longitude,
                        addressResponse = response.data
                    )
                )
            }
            is ApiResponse.Failure.Error -> return response
            is ApiResponse.Failure.Exception -> return response
        }
    }
}

sealed class LocationException(message: String) : Exception(message)
class LocationNotFoundException(message: String) : LocationException(message)
class AddressNotFoundException(message: String) : LocationException(message)
