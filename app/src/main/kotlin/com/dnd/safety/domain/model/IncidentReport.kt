package com.dnd.safety.domain.model

import android.net.Uri
import com.dnd.safety.data.model.Location

data class IncidentReport(
    val description: String,
    val disasterGroup: String,
    val location: Location,
    val images: List<Uri>
)