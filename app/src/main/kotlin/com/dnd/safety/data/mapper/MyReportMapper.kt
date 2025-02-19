package com.dnd.safety.data.mapper

import com.dnd.safety.data.model.response.MyReportResponse
import com.dnd.safety.domain.model.MyReports

fun MyReportResponse.toMyReports() = MyReports(
    incidents = data.incidents.map { it.toIncidents() },
    nextCursor = data.nextCursorRequest.key,
)