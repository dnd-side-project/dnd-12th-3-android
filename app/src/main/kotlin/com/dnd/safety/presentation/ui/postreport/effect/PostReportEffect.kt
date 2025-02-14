package com.dnd.safety.presentation.ui.postreport.effect

sealed interface PostReportEffect {
    data object NavigateBack : PostReportEffect
    data class ShowToast(val message: String) : PostReportEffect
}