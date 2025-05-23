package com.dnd.safety.data.repository

import com.dnd.safety.data.mapper.toIncidentList
import com.dnd.safety.data.remote.api.MyReportService
import com.dnd.safety.domain.model.IncidentList
import com.dnd.safety.domain.repository.MyReportRepository
import com.dnd.safety.utils.Logger
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import javax.inject.Inject

class MyReportRepositoryImpl @Inject constructor(
    private val myReportService: MyReportService
) : MyReportRepository {

    override suspend fun getMyReports(cursor: Long?): ApiResponse<IncidentList> {
        return myReportService
            .getMyReport(cursor)
            .mapSuccess {
                toIncidentList().also {
                    Logger.d("getMyReports: $it")
                }
            }.onFailure {
                Logger.e(message())
            }
    }
}