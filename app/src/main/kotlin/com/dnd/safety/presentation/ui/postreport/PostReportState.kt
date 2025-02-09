package com.dnd.safety.presentation.ui.postreport

import android.net.Uri
import com.dnd.safety.data.model.Location
import com.dnd.safety.domain.model.IncidentCategory

data class PostReportState(
    val content: String = "",
    val selectedCategory: IncidentCategory? = null,
    val imageUris: List<Uri> = emptyList(),
    val location: Location = Location(
        latitude = 0.0,
        longitude = 0.0,
        placeName = "",
        address = "위치를 선택해주세요"
    )
)