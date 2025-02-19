package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MyReportResponse(
    val code: String = "",
    val data: Data
) {

    @Serializable
    data class Data(
        val incidents: List<IncidentData>,
        val nextCursorRequest: NextCursorRequest
    )
}
