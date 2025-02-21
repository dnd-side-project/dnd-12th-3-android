package com.dnd.safety.presentation.ui.myPage.effect

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.Comment
import com.dnd.safety.domain.model.Incident

@Stable
sealed interface MyReportModalEffect {

    @Immutable
    data object Hidden : MyReportModalEffect

    @Immutable
    data class ShowIncidentsActionMenu(val incident: Incident) : MyReportModalEffect

    @Immutable
    data class ShowDeleteCheckDialog(val incident: Incident) : MyReportModalEffect
}