package com.dnd.safety.presentation.ui.postreport

sealed class PostReportEffect {
    data object NavigateBack : PostReportEffect()
    data class ShowToast(val message: String) : PostReportEffect()
    data object NavigateToPhotoSelection : PostReportEffect()
}