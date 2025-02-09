package com.dnd.safety.presentation.ui.postreport

import android.net.Uri
import com.dnd.safety.domain.model.IncidentCategory

data class PostReportState(
    val content: String = "",
    val selectedCategory: IncidentCategory? = null,
    val imageUris: List<Uri> = emptyList(),
    val location: String = "위치를 선택해주세요"
)