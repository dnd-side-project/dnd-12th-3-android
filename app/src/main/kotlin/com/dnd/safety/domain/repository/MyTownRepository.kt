package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.domain.model.Point
import com.skydoves.sandwich.ApiResponse

interface MyTownRepository {

    suspend fun getMyTownList(): ApiResponse<List<MyTown>>

    suspend fun addMyTown(
        title: String,
        address: String,
        point: Point,
        sido: String,
        sgg: String,
        emd: String
    ): ApiResponse<Unit>

    suspend fun deleteMyTown(id: Long): ApiResponse<Unit>
}