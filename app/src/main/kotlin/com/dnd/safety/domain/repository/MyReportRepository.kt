package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.Incident
import com.skydoves.sandwich.ApiResponse

interface MyReportRepository {
    suspend fun getMyReports(): ApiResponse<List<Incident>>
}