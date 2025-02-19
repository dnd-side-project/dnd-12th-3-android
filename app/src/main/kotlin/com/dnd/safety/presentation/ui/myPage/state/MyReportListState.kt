package com.dnd.safety.presentation.ui.myPage.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.Incident

@Stable
sealed interface MyReportListState {

    @Immutable
    data object Loading : MyReportListState

    @Immutable
    data object Empty : MyReportListState

    @Immutable
    data class Success(
        val myReports: List<Incident>,
        val nextCursor: Long
    ) : MyReportListState {

    }
}