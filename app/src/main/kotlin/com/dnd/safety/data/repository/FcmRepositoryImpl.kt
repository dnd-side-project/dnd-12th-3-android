package com.dnd.safety.data.repository

import com.dnd.safety.data.local.dao.FcmDao
import com.dnd.safety.data.local.entity.FcmEntity
import com.dnd.safety.data.mapper.toFcmMessage
import com.dnd.safety.domain.model.FcmMessage
import com.dnd.safety.domain.repository.FcmRepository
import com.dnd.safety.fcm.FcmDto
import com.dnd.safety.utils.toLocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val fcmDao: FcmDao
) : FcmRepository {

    override fun getFcmFlow(): Flow<FcmMessage> {
        return fcmDao.getFcmFlow().map { it.toFcmMessage() }
    }

    override suspend fun insertFcm(fcmDto: FcmDto) {
        try {
            fcmDao.insertFcm(
                FcmEntity(
                    title = fcmDto.title,
                    content = fcmDto.content,
                    imageUrl = fcmDto.imageUrl,
                    createAt = fcmDto.createAt.toLocalDateTime()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteFcm(fcmId: Long) {
        try {
            fcmDao.deleteFcm(fcmId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}