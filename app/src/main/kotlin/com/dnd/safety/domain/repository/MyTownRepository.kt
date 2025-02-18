package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.MyTown
import com.skydoves.sandwich.ApiResponse

interface MyTownRepository {

    suspend fun getMyTownList(): ApiResponse<List<MyTown>>

    suspend fun addMyTown(myTown: MyTown): ApiResponse<Unit>

    suspend fun deleteMyTown(id: Long): ApiResponse<Unit>
}