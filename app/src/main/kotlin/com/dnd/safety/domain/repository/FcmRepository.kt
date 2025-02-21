package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.FcmMessage
import com.dnd.safety.fcm.FcmDto
import kotlinx.coroutines.flow.Flow

interface FcmRepository {

    fun getFcmFlow(): Flow<List<FcmMessage>>

    suspend fun insertFcm(fcmDto: FcmDto)

    suspend fun deleteFcm(fcmId: Long)
}