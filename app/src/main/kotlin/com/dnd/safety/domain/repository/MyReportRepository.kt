package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.MyReports
import com.skydoves.sandwich.ApiResponse

interface MyReportRepository {
    suspend fun getMyReports(cursor: Long): ApiResponse<MyReports>
}