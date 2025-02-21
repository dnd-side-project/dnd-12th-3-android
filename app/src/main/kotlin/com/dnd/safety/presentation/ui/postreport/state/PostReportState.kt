package com.dnd.safety.presentation.ui.postreport.state

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.data.model.Location
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.model.IncidentCategory

@Stable
sealed interface PostReportState {

    @Immutable
    data object Loading : PostReportState

    data class Success(
        val content: String = "",
        val selectedCategory: IncidentCategory? = null,
        val imageUris: List<Uri> = emptyList(),
        val location: Location = Location(
            latitude = 0.0,
            longitude = 0.0,
            placeName = "",
            roadNameAddress = "",
            lotNumberAddress = ""
        ),
        val isLoading: Boolean = false,
        val fileUrls: List<String>? = null,
    ) : PostReportState {

        constructor(incident: Incident) : this(
            content = incident.description,
            selectedCategory = incident.incidentCategory,
            location = Location(incident),
            imageUris = emptyList(),
            fileUrls = incident.mediaFiles.map { it.fileUrl }
        )
    }
}