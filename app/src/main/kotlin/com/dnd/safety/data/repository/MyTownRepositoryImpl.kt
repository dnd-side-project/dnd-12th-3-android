package com.dnd.safety.data.repository

import com.dnd.safety.data.mapper.toMyTowns
import com.dnd.safety.data.model.request.MyTownRequest
import com.dnd.safety.data.remote.api.MyTownService
import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.domain.model.Point
import com.dnd.safety.domain.repository.MyTownRepository
import com.dnd.safety.utils.Logger
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import javax.inject.Inject

class MyTownRepositoryImpl @Inject constructor(
    private val myTownService: MyTownService
) : MyTownRepository {

    override suspend fun getMyTownList(): ApiResponse<List<MyTown>> {
        return myTownService
            .getMyTowns()
            .mapSuccess {
                toMyTowns()
            }.onFailure {
                Logger.e(message())
            }.onError {
                Logger.e(message())
            }
    }

    override suspend fun addMyTown(
        title: String,
        address: String,
        point: Point
    ): ApiResponse<Unit> {
        return myTownService
            .addMyTown(
                MyTownRequest(
                    addressName = address,
                    latitude = point.y,
                    longitude = point.x,
                )
            )
            .mapSuccess {}
            .onFailure {
                Logger.e(message())
            }.onError {
                Logger.e(message())
            }
    }

    override suspend fun deleteMyTown(id: Long): ApiResponse<Unit> {
        return myTownService
            .deleteMyTown(id.toLong())
            .mapSuccess {}
            .onFailure {
                Logger.e(message())
            }.onError {
                Logger.e(message())
            }
    }
}