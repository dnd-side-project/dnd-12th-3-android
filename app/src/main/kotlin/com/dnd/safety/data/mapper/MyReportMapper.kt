package com.dnd.safety.data.mapper

import com.dnd.safety.data.model.response.IncidentData
import com.dnd.safety.data.model.response.MyReportResponse
import com.dnd.safety.domain.model.IncidentList
import com.dnd.safety.domain.model.MyReports

fun MyReportResponse.toMyReports() = IncidentList(
    incidents = data.incidents.map(IncidentData::toIncidents),
    nextCursor = data.nextCursorRequest.key,
)