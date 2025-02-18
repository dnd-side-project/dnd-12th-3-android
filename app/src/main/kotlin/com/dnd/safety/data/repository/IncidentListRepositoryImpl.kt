package com.dnd.safety.data.repository

import com.dnd.safety.data.remote.api.IncidentListService
import com.dnd.safety.data.mapper.toIncidentsList
import com.dnd.safety.domain.model.BoundingBox
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.repository.IncidentListRepository
import com.dnd.safety.utils.Logger
import com.google.android.gms.maps.model.LatLng
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import javax.inject.Inject

class IncidentListRepositoryImpl @Inject constructor(
    private val incidentListService: IncidentListService
) : IncidentListRepository {

    override suspend fun getIncidents(
        boundingBox: BoundingBox,
        myLocation: LatLng
    ): ApiResponse<List<Incident>> {
        return incidentListService.getIncidents(
            topRightX = boundingBox.topRight.x,
            topRightY = boundingBox.topRight.y,
            bottomLeftX = boundingBox.bottomleft.x,
            bottomLeftY = boundingBox.bottomleft.y,
            myX = myLocation.latitude,
            myY = myLocation.longitude
        ).mapSuccess {
            toIncidentsList()
        }.onFailure {
            Logger.e(message())
        }.onError {
            Logger.e(message())
        }
    }

    override suspend fun getMyIncidents(): ApiResponse<List<Incident>> {
        TODO("Not yet implemented")
    }
}